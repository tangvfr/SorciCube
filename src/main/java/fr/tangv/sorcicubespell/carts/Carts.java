package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	private Map<String, Cart> carts;
	
	public Carts(SorciCubeSpell sorci, MongoCollection<Document> cartsCol) {
		this.sorci = sorci;
		this.cartsCol = cartsCol;
		reload();
	}
	
	public void reload() {
		this.carts = new ConcurrentHashMap<String, Cart>();
		for(Document doc : cartsCol.find()) {
			Cart cart = documentToCart(doc);
			if (cart != null)
				carts.put(cart.id, cart);
		}
	}
	
	public Collection<Cart> getCarts() {
		return carts.values();
	}
	
	public Cart getCart(String id) {
		if (carts.containsKey(id))
			return carts.get(id);
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
		lore.add("§7"+this.sorci.getEnumTool().typeToString(cart.type));
		lore.add("§7"+this.sorci.getEnumTool().rarityToString(cart.rarity));
		lore.add("§7"+this.sorci.getEnumTool().factionToString(cart.faction));
		lore.add("");
		lore.add("§8Id: "+cart.id);
		//lore
		for (int i = 0; i < cart.description.length; i++)
			lore.add(cart.description[i]);
		//return item
		return ItemBuild.buildItem(cart.material.getItemType(),
				amount,
				(short) 0,
				cart.material.getData(),
				cart.name,
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
		} else if (cart instanceof CartEntity) {
			CartEntity cartEntity = (CartEntity) cart;
			doc.append("health", cartEntity.getHealth());
		} else {
			return null;
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
		carts.put(cart.id, cart);
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
		carts.put(cart.id, cart);
		return cart;
	}
	
	public void update(Cart cart) {
		Document doc = new Document("_id", new ObjectId(cart.getId()));
		cartsCol.findOneAndReplace(doc, cartToDocument(cart));
		carts.replace(cart.id, cart);
	}
	
	public void delete(Cart cart) {
		if (carts.containsKey(cart.id))
			carts.remove(cart.id);
		Document doc = new Document("_id", new ObjectId(cart.id));
		Iterator<Document> rep = cartsCol.find(doc).iterator();
		if (rep.hasNext())
			cartsCol.deleteOne(doc);
	}
	
}
