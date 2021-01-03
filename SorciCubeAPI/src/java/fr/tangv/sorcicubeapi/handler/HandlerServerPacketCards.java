package fr.tangv.sorcicubeapi.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Vector;
import java.util.function.Predicate;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerPacketCards implements RequestHandlerInterface {

	private final Vector<Document> packets;
	private final File file;
	
	public HandlerServerPacketCards() throws IOException {
		this.file = new File("./packet_cards.json");
		this.packets = new Vector<Document>();
		load();
	}
	
	public synchronized void load() throws IOException {
		if (!file.exists()) {
			file.createNewFile();
			save();
		} else {
			if (file.isFile()) {
				FileInputStream in = new FileInputStream(file);
				byte[] buf = new byte[(int) file.length()];
				in.read(buf);
				in.close();
				Document doc = Document.parse(new String(buf, Client.CHARSET));
				buf = new byte[0];
				packets.clear();
				for (Document d : doc.getList("list", Document.class))
					packets.add(d);
			} else
				throw new IOException("Error not file, \"packet_cards.json\" is directory !");
		}
	}
	
	public synchronized void save() throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		out.write(new Document("list", packets)
			.toJson().getBytes(Client.CHARSET));
		out.close();
	}

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.PACKETS_GET_ALL)
	public void getAll(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.PACKETS_LIST, new Document("list", packets).toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PACKETS_NEW)
	public void newPacket(Client client, Request request) throws IOException, RequestException {
		try {
			Document doc = PacketCards.createNeutralPacketCards(request.data).toDocument();
			String name = doc.getString("name");
			for (Document d : packets)
				if (d.getString("name").equals(name))
					throw new Exception("Already packets with this name !");
			packets.add(doc);
			save();
			client.sendRequest(request.createReponse(RequestType.PACKETS_NEWED, doc.toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PACKETS_REMOVE)
	public void removePacket(Client client, Request request) throws IOException, RequestException {
		try {
			packets.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document doc) {
					return doc.getString("name").equals(request.data);
				}
			});
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PACKETS_UPDATE)
	public void updatePacket(Client client, Request request) throws IOException, RequestException {
		try {
			Document doc = Document.parse(request.data);
			String name = new String(Base64.getDecoder().decode(request.name), Client.CHARSET);
			packets.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document doc) {
					return doc.getString("name").equals(name);
				}
			});
			packets.add(doc);
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
