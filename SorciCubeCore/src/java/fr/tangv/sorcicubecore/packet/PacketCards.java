package fr.tangv.sorcicubecore.packet;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;

public class PacketCards {

	private String name;
	private int[] faction;
	private int[] rarity;
	private int[] type;
	private int size;
	private int idItem;
	
	public PacketCards(String name,
						int[] faction,
						int[] rarity,
						int[] type,
						int size,
						int idItem) {
		this.name = name;
		this.faction = faction;
		this.rarity = rarity;
		this.type = type;
		this.size = size;
		this.idItem = idItem;
	}
	
	public int[] getFaction() {
		return faction;
	}
	
	public int[] getRarity() {
		return rarity;
	}
	
	public int[] getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getId() {
		return idItem;
	}
	
	public Document toDocument() {
		Document doc = new Document("name", name)
			.append("size", size)
			.append("id_item", idItem);
		CardFaction[] factions = CardFaction.values();
		for (int i = 0; i < faction.length; i++)
			doc.append("FACTION_"+factions[i].name(), faction[i]);
		CardRarity[] raritys = CardRarity.values();
		for (int i = 0; i < raritys.length; i++)
			doc.append("RARITY_"+raritys[i].name(), rarity[i]);
		CardType[] types = CardType.values();
		for (int i = 0; i < type.length; i++)
			doc.append("TYPE_"+types[i].name(), type[i]);
		return doc;
	}
	
	public static PacketCards toPacketCards(Document doc) {
		String name = doc.getString("name");
		int size = doc.getInteger("size");
		int idItem = doc.getInteger("id_item");
		CardFaction[] factions = CardFaction.values();
		int[] faction = new int[factions.length];
		for (int i = 0; i < faction.length; i++)
			faction[i] = doc.getInteger("FACTION_"+factions[i]);
		CardRarity[] raritys = CardRarity.values();
		int[] rarity = new int[raritys.length];
		for (int i = 0; i < rarity.length; i++)
			rarity[i] = doc.getInteger("RARITY_"+raritys[i]);
		CardType[] types = CardType.values();
		int[] type = new int[types.length];
		for (int i = 0; i < type.length; i++)
			type[i] = doc.getInteger("TYPE_"+types[i]);
		return new PacketCards(name, faction, rarity, type, size, idItem);
	}
	
	public static PacketCards createNeutralPacketCards(String name) {
		int size = 3;
		int idItem = 340;//book id
		int[] faction = new int[CardFaction.values().length];
		for (int i = 0; i < faction.length; i++)
			faction[i] = 100/faction.length;
		int[] rarity = new int[CardRarity.values().length];
		for (int i = 0; i < rarity.length; i++)
			rarity[i] = 100/rarity.length;
		int[] type = new int[CardType.values().length];
		for (int i = 0; i < type.length; i++)
			type[i] = 100/type.length;
		return new PacketCards(name, faction, rarity, type, size, idItem);
	}
	
}
