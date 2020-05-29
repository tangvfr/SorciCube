package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class Carts {

	private SorciCubeSpell sorci;
	private MongoCollection<Document> cartsCol;
	
	public Carts(SorciCubeSpell sorci, MongoCollection<Document> cartsCol) {
		this.sorci = sorci;
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
	
	public ItemStack cartToItem(Cart cart) {
		return this.cartToItem(cart, 1, false);
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack cartToItem(Cart cart, int amount, boolean ench) {
		ArrayList<String> lore = new ArrayList<String>();
		//type
		lore.add("");
		lore.add("§7"+this.sorci.getEnumTool().typeToString(cart.getType()));
		lore.add("§7"+this.sorci.getEnumTool().rarityToString(cart.getRarity()));
		lore.add("§7"+this.sorci.getEnumTool().factionToString(cart.getFaction()));
		//lore
		lore.add("");
		for (int i = 0; i < cart.getDescription().length; i++)
			lore.add(cart.getDescription()[i]);
		//id
		lore.add("");
		lore.add("§8Id: "+cart.getId());
		//return item
		return ItemBuild.buildItem(cart.getMaterial().getItemType(),
				amount,
				(short) 0,
				cart.getMaterial().getData(),
				cart.getName(),
				lore,
				ench);
	}
	
	public Cart documentToCart(Document doc) {
		String id = doc.getObjectId("_id").toHexString();
		String[] mat = doc.getString("material").split(":");
		@SuppressWarnings("deprecation")
		MaterialData material = new MaterialData(Integer.parseInt(mat[0]), Byte.parseByte(mat[1]));
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
			cart = new CartSort(id, material, name, description, countMana, damage, rarity, faction, heal, giveMana, cible);
		} else if (type == CartType.ENTITY) {
			int health = doc.getInteger("health");
			cart = new CartEntity(id, material, name, description, countMana, damage, rarity, faction, health);
		}
		return cart;
	}
	
	@SuppressWarnings("deprecation")
	public Document cartToDocument(Cart cart) {
		Document doc = new Document("_id", new ObjectId(cart.getId()));
		MaterialData mat = cart.getMaterial();
		doc.append("material", mat.getItemTypeId()+":"+mat.getData());
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
		}
		return doc;
	}
	
	public CartSort newCartSort() {
		CartSort cart = new CartSort(
				new ObjectId().toHexString(),
				new MaterialData(Material.BLAZE_POWDER),
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
				"§4NoName",
				new String[0],
				2,
				3,
				CartRarity.COMMUN,
				CartFaction.LIGHT,
				4
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
