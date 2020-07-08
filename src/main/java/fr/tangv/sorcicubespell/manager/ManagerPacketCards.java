package fr.tangv.sorcicubespell.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.packet.CommandPacketAdd;
import fr.tangv.sorcicubespell.packet.CommandPacketGive;
import fr.tangv.sorcicubespell.packet.EventPacket;
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
		//event
		EventPacket eventPacket = new EventPacket(this);
		Bukkit.getPluginManager().registerEvents(eventPacket, sorci);
		//timer
		Bukkit.getScheduler().runTaskTimerAsynchronously(sorci, eventPacket, 0, 4);
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
	
	private int chooseIndex(int[] stat) throws Exception {
		int max = 0;
		for (int num : stat) {
			if (num < 0)
				throw new Exception("Negative number");
			max += num;
		}
		if (max <= 0)
			throw new Exception("Total stat is 0");
		int number = (int) (Math.random()*max)+1;
		int progression = 0;
		for (int i = 0; i < stat.length; i++) {
			progression += stat[i];
			if (number <= progression)
				return i;
		}
		return -1;
	}
	
	public Card[] packetTakeCard(PacketCards packet, Collection<Card> collCards) throws Exception {
		Card[] cards = new Card[packet.getSize()];
		ArrayList<Card> collectionCards = new ArrayList<Card>(collCards);
		for (int i = 0; i < cards.length; i++) {
			CardFaction faction = CardFaction.values()[chooseIndex(packet.getFaction())];
			CardRarity rarity = CardRarity.values()[chooseIndex(packet.getRarity())];
			CardType type = CardType.values()[chooseIndex(packet.getType())];
			ArrayList<Card> list = new ArrayList<Card>();
			for (Card card : collectionCards) {
				boolean hide = false;
				for (CardFeature feature : card.getFeatures().listFeatures())
					if (feature.getType() == CardFeatureType.HIDE_CART) {
						hide = true;
						break;
					}
				if (!hide && card.getFaction() == faction && card.getRarity() == rarity && card.getType() == type)
					list.add(card);
			}
			if (list.size() <= 0)
				throw new Exception("Not found card for the filter");
			Card card = list.get((int) (Math.random()*list.size()));
			collectionCards.remove(card);
			cards[i] = card;
		}
		return cards;
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
		for (Document doc : packets.find())
			list.add(doc.getString("name"));
		return list;
	}
	
}
