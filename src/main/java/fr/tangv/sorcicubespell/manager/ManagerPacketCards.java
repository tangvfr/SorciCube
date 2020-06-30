package fr.tangv.sorcicubespell.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.packet.CommandPacketAdd;
import fr.tangv.sorcicubespell.packet.CommandPacketGive;
import fr.tangv.sorcicubespell.packet.PacketCards;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class ManagerPacketCards {

	public SorciCubeSpell sorci;
	public MongoCollection<Document> packets;
	
	public ManagerPacketCards(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.packets = sorci.getMongo().getPackets();
		//command
		sorci.getCommand("packetadd").setExecutor(new CommandPacketAdd(this));
		CommandPacketGive commandPacketGive = new CommandPacketGive(this);
		sorci.getCommand("packetgive").setExecutor(commandPacketGive);
		sorci.getCommand("packetgive").setTabCompleter(commandPacketGive);
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack packetToItem(PacketCards packet) {
		List<String> lore = new ArrayList<String>();
		//faction
		lore.add("");
		CardFaction[] factions = CardFaction.values();
		int[] faction = packet.getFaction();
		for (int i = 0; i < faction.length; i++)
			lore.add(
				sorci.getParameter().getString("packet_format")
					.replace("{name}", sorci.getEnumTool().factionToString(factions[i]))
					.replace("{value}", Integer.toString(faction[i]))
				);
		//rarity
		lore.add("");
		CardRarity[] raritys = CardRarity.values();
		int[] rarity = packet.getRarity();
		for (int i = 0; i < rarity.length; i++)
			lore.add(
				sorci.getParameter().getString("packet_format")
					.replace("{name}", sorci.getEnumTool().rarityToString(raritys[i]))
					.replace("{value}", Integer.toString(rarity[i]))
				);
		//type
		lore.add("");
		CardType[] types = CardType.values();
		int[] type = packet.getType();
		for (int i = 0; i < type.length; i++)
			lore.add(
				sorci.getParameter().getString("packet_format")
					.replace("{name}", sorci.getEnumTool().typeToString(types[i]))
					.replace("{value}", Integer.toString(type[i]))
				);
		//number
		lore.add("");
		lore.add(sorci.getParameter().getString("packet_size").replace("{value}", Integer.toString(packet.getSize())));
		//packet end lore
		lore.add("");
		lore.add(sorci.getParameter().getString("packet_lore"));
		return ItemBuild.buildItem(Material.getMaterial(packet.getId()), 1, (short) 0, (byte) 0, packet.getName(), lore, false);
	}
	
	public boolean itemIsPacket(ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName()) {
			List<String> lore = item.getItemMeta().getLore();
			if (lore.size() >= 1) {
				String text = lore.get(lore.size()-1);
				return sorci.getParameter().getString("packet_lore").equals(text);
			}
		}
		return false;
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	private Document nameDocument(String name) {
		return new Document("name", name);
	}
	
	public void newPacket(String name) {
		packets.insertOne(PacketCards.createNeutralPacketCards(name).toDocument());
	}
	
	public boolean containtPacket(String name) {
		Iterator<Document> rep = packets.find(nameDocument(name)).iterator();
		return rep.hasNext();
	}
	
	public PacketCards getPacketCards(String name) {
		Iterator<Document> rep = packets.find(nameDocument(name)).iterator();
		if (rep.hasNext())
			return PacketCards.toPacketCards(rep.next());
		else
			return null;
	}
	
	public List<String> getListNamePacket() {
		List<String> list = new ArrayList<String>();
		for (Document doc : packets.listIndexes())
			list.add(doc.getString("name"));
		return list;
	}
	
}
