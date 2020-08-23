package fr.tangv.sorcicubespell.manager;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.command.CommandPacketAdd;
import fr.tangv.sorcicubespell.command.CommandPacketGive;
import fr.tangv.sorcicubespell.packet.EventPacket;
import fr.tangv.sorcicubespell.packet.PacketCards;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class ManagerPacketCards {

	public SorciCubeSpell sorci;
	public MongoCollection<Document> packetsMongo;
	public ConcurrentHashMap<String, PacketCards> packets;
	
	public ManagerPacketCards(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.packetsMongo = sorci.getMongo().getPackets();
		this.refrech();
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
	
	public void refrech() {
		this.packets = new ConcurrentHashMap<String, PacketCards>();
		for (Document doc : packetsMongo.find()) {
			PacketCards packet = PacketCards.toPacketCards(doc);
			packets.put(packet.getName(), packet);
		}	
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack packetToItem(PacketCards packet) {
		ArrayList<String> lore = new ArrayList<String>();
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
	
	private int chooseIndex(int[] stat, Random random) throws Exception {
		int max = 0;
		for (int num : stat) {
			if (num < 0)
				throw new Exception("Negative number");
			max += num;
		}
		if (max <= 0)
			throw new Exception("Total stat is 0");
		int number = random.nextInt(max)+1;
		int progression = 0;
		for (int i = 0; i < stat.length; i++) {
			progression += stat[i];
			if (number <= progression)
				return i;
		}
		return -1;
	}
	
	public Card[] packetTakeCard(PacketCards packet) throws Exception {
		Random random = new Random();
		Card[] cards = new Card[packet.getSize()];
		Vector<Card> collectionCards = sorci.getManagerCards().cloneCardsValue();
		for (int i = 0; i < cards.length; i++) {
			CardFaction faction = CardFaction.values()[chooseIndex(packet.getFaction(), random)];
			CardRarity rarity = CardRarity.values()[chooseIndex(packet.getRarity(), random)];
			CardType type = CardType.values()[chooseIndex(packet.getType(), random)];
			ArrayList<Card> list = new ArrayList<Card>();
			for (Card card : collectionCards) {
				if (!card.getFeatures().hasFeature(CardFeatureType.HIDE_CARD) && card.getFaction() == faction && card.getRarity() == rarity && card.getType() == type)
					list.add(card);
			}
			if (list.size() <= 0)
				throw new Exception("Not found card for the filter");
			Card card = list.get(random.nextInt(list.size()));
			collectionCards.remove(card);
			cards[i] = card;
		}
		return cards;
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public void newPacket(String name) {
		PacketCards packet = PacketCards.createNeutralPacketCards(name);
		packetsMongo.insertOne(packet.toDocument());
		packets.put(name, packet);
	}
	
	public boolean containtPacket(String name) {
		return packets.containsKey(name);
	}
	
	public PacketCards getPacketCards(String name) {
		return packets.get(name);
	}
	
	public Enumeration<String> getEnumNamePacket() {
		return packets.keys();
	}
	
}