package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.mongodb.client.MongoCollection;

public class Carts {

	private MongoCollection<Document> cartsCol;
	
	public Carts(MongoCollection<Document> cartsCol) {
		this.cartsCol = cartsCol;
	}
	
	public ArrayList<Cart> getCarts() {
		ArrayList<Cart> list = new ArrayList<Cart>();
		for(Document doc : cartsCol.find()) {
			Cart cart = documentToCart(doc);
			list.add(cart);
		}
		return list;
	}
	
	public Cart getCart(String id) {
		Document doc = new Document("_id", new ObjectId(id));
		Iterator<Document> rep = cartsCol.find(doc).iterator();
		if (rep.hasNext())
			return documentToCart(rep.next());
		else
			return null;
	}
	
	public Cart documentToCart(Document doc) {
		String id = doc.getObjectId("_id").toHexString();
		String[] mat = doc.getString("material").split(":");
		@SuppressWarnings("deprecation")
		MaterialData material = new MaterialData(Integer.parseInt(mat[0]), Byte.parseByte(mat[1]));
		String materialURL = doc.getString("material_url");
		String name = doc.getString("name");
		String[] description = doc.getList("description", String.class).toArray(new String[0]);
		int countMana = doc.getInteger("countMana");
		int damage = doc.getInteger("damage");
		CartType type = CartType.valueOf(doc.getString("type"));
		CartRarity rarity = CartRarity.valueOf(doc.getString("rarity"));
		CartFaction faction = CartFaction.valueOf(doc.getString("faction"));
		Cart cart = null;
		if (type == CartType.SORT) {
			int heal = doc.getInteger("heal");
			int giveMana = doc.getInteger("giveMana");
			CartCible cible = CartCible.valueOf(doc.getString("cible"));
			cart = new CartSort(id, material, materialURL, name, description, countMana, damage, rarity, faction, heal, giveMana, cible);
		} else if (type == CartType.ENTITY) {
			int health = doc.getInteger("health");
			String skin = doc.getString("skin");
			cart = new CartEntity(id, material, materialURL, name, description, countMana, damage, rarity, faction, health, skin);
		}
		return cart;
	}
	
	@SuppressWarnings("deprecation")
	public Document cartToDocument(Cart cart) {
		Document doc = new Document("_id", new ObjectId(cart.getId()));
		MaterialData mat = cart.getMaterial();
		doc.append("material", mat.getItemTypeId()+":"+mat.getData());
		doc.append("material_url", cart.getMaterialURL());
		doc.append("name", cart.getName());
		doc.append("description", Arrays.asList(cart.getDescription()));
		doc.append("countMana", cart.getCountMana());
		doc.append("damage", cart.getDamage());
		doc.append("type", cart.getType().name());
		doc.append("rarity", cart.getRarity().name());
		doc.append("faction", cart.getFaction().name());
		if (cart instanceof CartSort) {
			CartSort cartSort = (CartSort) cart;
			doc.append("heal", cartSort.getHeal());
			doc.append("giveMana", cartSort.getGiveMana());
			doc.append("cible", cartSort.getCible().name());
		} else {
			CartEntity cartEntity = (CartEntity) cart;
			doc.append("health", cartEntity.getHealth());
			doc.append("skin", cartEntity.getSkin());
		}
		return doc;
	}
	
	public CartSort newCartSort() {
		CartSort cart = new CartSort(
				new ObjectId().toHexString(),
				new MaterialData(Material.BLAZE_POWDER),
				null,
				"§4NoName",
				new String[0],
				2,
				3,
				CartRarity.COMMUN,
				CartFaction.LIGHT,
				-1,
				-1,
				CartCible.ONE_ENTITY_ENEMIE
			);
		cartsCol.insertOne(cartToDocument(cart));
		return cart;
	}
	
	public CartEntity newCartEntity() {
		CartEntity cart = new CartEntity(
				new ObjectId().toHexString(),
				new MaterialData(Material.ROTTEN_FLESH),
				null,
				"§4NoName",
				new String[0],
				2,
				3,
				CartRarity.COMMUN,
				CartFaction.LIGHT,
				4,
				null
			);
		cartsCol.insertOne(cartToDocument(cart));
		return cart;
	}
	
	public void update(Cart cart) {
		Document doc = new Document("_id", new ObjectId(cart.getId()));
		Iterator<Document> rep = cartsCol.find(doc).iterator();
		if (rep.hasNext())
			cartsCol.replaceOne(rep.next(), cartToDocument(cart));
	}
	
	public void delete(Cart cart) {
		Document doc = new Document("_id", new ObjectId(cart.getId()));
		Iterator<Document> rep = cartsCol.find(doc).iterator();
		if (rep.hasNext())
			cartsCol.deleteOne(rep.next());
	}
	
}
