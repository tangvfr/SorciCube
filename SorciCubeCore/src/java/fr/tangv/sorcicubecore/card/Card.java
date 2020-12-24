package fr.tangv.sorcicubecore.card;

import java.util.List;
import java.util.UUID;
import org.bson.Document;

public class Card {
	
	private final UUID uuid;
	private CardMaterial material;
	private String name;
	private CardType type;
	private CardRarity rarity;
	private CardFaction faction;
	private CardCible cible;
	private CardFaction cibleFaction;
	private int mana;
	private CardFeatures features;
	private List<String> description;
	private boolean originalName;
	
	public Card(UUID uuid,
			CardMaterial material,
			String name,
			CardType type,
			CardRarity rarity,
			CardFaction faction,
			CardCible cible,
			CardFaction cibleFaction,
			int mana,
			CardFeatures features,
			List<String> description,
			boolean originalName) {
		this.uuid = uuid;
		this.setMaterial(material);
		this.setName(name);
		this.setType(type);
		this.setRarity(rarity);
		this.setFaction(faction);
		this.setCible(cible);
		this.setCibleFaction(cibleFaction);
		this.setMana(mana);
		this.setFeatures(features);
		this.setDescription(description);
		this.setOriginalName(originalName);
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public CardMaterial getMaterial() {
		return material;
	}

	public void setMaterial(CardMaterial material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}
	
	public String renderName() {
		if (isOriginalName())
			return "§r"+name;
		else
			return "§r"+rarity.getColor()+"["+faction.getColor()+name+rarity.getColor()+"]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

	public CardRarity getRarity() {
		return rarity;
	}

	public void setRarity(CardRarity rarity) {
		this.rarity = rarity;
	}

	public CardFaction getFaction() {
		return faction;
	}

	public void setFaction(CardFaction faction) {
		this.faction = faction;
	}

	public CardCible getCible() {
		return cible;
	}

	public void setCible(CardCible cible) {
		this.cible = cible;
	}

	public CardFaction getCibleFaction() {
		return cibleFaction;
	}

	public void setCibleFaction(CardFaction cibleFaction) {
		this.cibleFaction = cibleFaction;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public CardFeatures getFeatures() {
		return features;
	}

	public void setFeatures(CardFeatures features) {
		this.features = features;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}
	
	public boolean isOriginalName() {
		return originalName;
	}

	public void setOriginalName(boolean originalName) {
		this.originalName = originalName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Card)
			return ((Card) obj).getUUID().equals(this.getUUID());
		return false;
	}

	public Card clone() {
		return Card.toCard(toDocument());
	}
	
	public Document toDocument() {
		Document document = new Document("version", 2)
			.append("uuid", this.uuid.toString())
			.append("material", this.material.toDocument())
			.append("name", this.name)
			.append("type", this.type.name())
			.append("rarity", this.rarity.name())
			.append("faction", this.faction.name())
			.append("cible", this.cible.name())
			.append("ciblefaction", this.cibleFaction.name())
			.append("mana", this.mana)
			.append("features", this.features.toDocument())
			.append("description", this.description)
			.append("original_name", this.originalName);
		return document;
	}
	
	public static Card toCard(Document document) {
		return new Card(
				UUID.fromString(document.getString("uuid")),
				CardMaterial.toCartMaterial(document.get("material", Document.class)),
				document.getString("name"),
				CardType.valueOf(document.getString("type")),
				CardRarity.valueOf(document.getString("rarity")),
				CardFaction.valueOf(document.getString("faction")),
				CardCible.valueOf(document.getString("cible")),
				CardFaction.valueOf(document.getString("ciblefaction")),
				document.getInteger("mana").byteValue(),
				CardFeatures.toCardFeatures(document.get("features", Document.class)),
				document.getList("description", String.class),
				document.containsKey("original_name") ? document.getBoolean("original_name") : false
			);
	}
	
	public Document toUUIDDocument() {
		return Card.toUUIDDocument(this.uuid);
	}
	
	public static Document toUUIDDocument(UUID uuid) {
		return new Document("uuid", uuid.toString());
	}

}
