package fr.tangv.sorcicubecore.handler;

import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.packet.PacketCards;

import java.util.Enumeration;

public class HandlerPacketCardsR {

	public ConcurrentHashMap<String, PacketCards> packets;
	
	public HandlerPacketCardsR() {
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