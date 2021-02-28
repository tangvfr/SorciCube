package fr.tangv.sorcicubecore.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

import java.io.IOException;
import java.util.Base64;

public class HandlerPacketCards {

	private final SorciClient sorci;
	private final ConcurrentHashMap<String, PacketCards> packets;
	
	public HandlerPacketCards(SorciClient sorci) throws IOException, ReponseRequestException, RequestException {
		this.packets = new ConcurrentHashMap<String, PacketCards>();
		this.sorci = sorci;
		this.refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.PACKETS_GET_ALL, Request.randomID(), "List", null),
				RequestType.PACKETS_LIST);
		Document list = Document.parse(reponse.data);
		this.packets.clear();
		for (Document doc : list.getList("list", Document.class)) {
			PacketCards packet = PacketCards.toPacketCards(doc);
			packets.put(packet.getName(), packet);
		}
	}
	
	public void newPacket(String name) throws IOException, ReponseRequestException, RequestException {
		if (name == null || name.isEmpty())
			throw new RequestException("Packet new has name invalid");
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.PACKETS_NEW, Request.randomID(), "New", name),
				RequestType.PACKETS_NEWED);
		packets.put(name, PacketCards.toPacketCards(Document.parse(reponse.data)));
	}
	
	public void removePacket(String name) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.PACKETS_REMOVE, Request.randomID(), "Remove", name),
				RequestType.SUCCESSFUL);
		packets.remove(name);
	}
	
	public void updatePacket(String lastName, PacketCards packet) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.PACKETS_UPDATE, Request.randomID(), Base64.getEncoder().encodeToString(lastName.getBytes(Client.CHARSET)), packet.toDocument().toJson()),
				RequestType.SUCCESSFUL);
		packets.remove(lastName);
		packets.put(packet.getName(), packet);
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