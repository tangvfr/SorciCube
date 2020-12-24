package fr.tangv.sorcicubescore.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Enumeration;

import fr.tangv.sorcicubespell.packet.PacketCards;

public class HandlerPacketCards {

	public ConcurrentHashMap<String, PacketCards> packets;
	
	public HandlerPacketCards() {
		/*this.sorci = sorci;
		this.packetsMongo = sorci.getMongo().getPackets();
		this.refresh();*/
	}
	
	public void refresh() {
		/*this.packets = new ConcurrentHashMap<String, PacketCards>();
		for (Document doc : packetsMongo.find()) {
			PacketCards packet = PacketCards.toPacketCards(doc);
			packets.put(packet.getName(), packet);
		}	*/
	}
	
	public void newPacket(String name) {
		/*PacketCards packet = PacketCards.createNeutralPacketCards(name);
		packetsMongo.insertOne(packet.toDocument());
		packets.put(name, packet);*/
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