package fr.tangv.sorcicubespell.carts;

import java.util.UUID;

import org.bson.Document;

public class Cart {
	
	private UUID uuid;
	private CartMaterial material;
	private String name;
	private CartType type;
	private CartRarity rarity;
	private CartFaction faction;
	private int mana;
	private CartFeatures features;
	private int health;
	private String skin;
	
	public Cart(UUID uuid,
			CartMaterial material,
			String name,
			CartType type,
			CartRarity rarity,
			CartFaction faction,
			int mana,
			CartFeatures features,
			int health,
			String skin) {
		this.uuid = uuid;
		this.setMaterial(material);
		this.setName(name);
		this.setType(type);
		this.setRarity(rarity);
		this.setMana(mana);
		this.setFeatures(features);
		this.setHealth(health);
		this.setSkin(skin);
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public CartMaterial getMaterial() {
		return material;
	}

	public void setMaterial(CartMaterial material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CartType getType() {
		return type;
	}

	public void setType(CartType type) {
		this.type = type;
	}

	public CartRarity getRarity() {
		return rarity;
	}

	public void setRarity(CartRarity rarity) {
		this.rarity = rarity;
	}

	public CartFaction getFaction() {
		return faction;
	}

	public void setFaction(CartFaction faction) {
		this.faction = faction;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public CartFeatures getFeatures() {
		return features;
	}

	public void setFeatures(CartFeatures features) {
		this.features = features;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cart)
			return ((Cart) obj).getUUID().equals(this.getUUID());
		return false;
	}

	public Document toDocument() {
		Document document = new Document()
			.append("uuid", this.uuid.toString())
			.append("material", this.material.toDocument())
			.append("name", this.name)
			.append("type", this.type.name())
			.append("rarity", this.rarity.name())
			.append("faction", this.faction.name())
			.append("mana", this.mana)
			.append("features", this.features.toDocument())
			.append("health", this.health)
			.append("skin", this.skin);
		return document;
	}
	
	public static Cart toCart(Document document) {
		return new Cart(
				UUID.fromString(document.getString("uuid")),
				CartMaterial.toCartMaterial(document.get("material", Document.class)),
				document.getString("name"),
				CartType.valueOf(document.getString("type")),
				CartRarity.valueOf(document.getString("rarity")),
				CartFaction.valueOf(document.getString("faction")),
				document.getInteger("mana"),
				CartFeatures.toCartFeatures(document.get("features", Document.class)),
				document.getInteger("health"),
				document.getString("skin")
			);
	}
	
	public Document toUUIDDocument() {
		return Cart.toUUIDDocument(this.uuid);
	}
	
	public static Document toUUIDDocument(UUID uuid) {
		return new Document("uuid", uuid.toString());
	}
	
}
