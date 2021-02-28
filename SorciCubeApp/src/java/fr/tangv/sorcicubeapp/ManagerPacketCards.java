package fr.tangv.sorcicubeapp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubecore.packet.PacketCards;

public class ManagerPacketCards {

	public MongoCollection<Document> packetsMongo;
	public ConcurrentHashMap<String, PacketCards> packets;
	
	public ManagerPacketCards(MongoDBManager manager) {
		this.packetsMongo = manager.getPackets();
		this.refresh();
	}
	
	public void refresh() {
		this.packets = new ConcurrentHashMap<String, PacketCards>();
		for (Document doc : packetsMongo.find()) {
			PacketCards packet = PacketCards.toPacketCards(doc);
			packets.put(packet.getName(), packet);
		}	
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
	
	public KeySetView<String, PacketCards> getPackets() {
		return packets.keySet();
	}
	
}