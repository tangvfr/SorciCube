package fr.tangv.sorcicubeapi.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerCards implements RequestHandlerInterface {

	private final ConcurrentHashMap<UUID, Card> cards;//---------------
	private final File file;
	
	public HandlerServerCards() {
		this.file = new File("./cards.json");
		this.cards = new ConcurrentHashMap<UUID, Card>();
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
				Document d = Document.parse(new String(buf, Client.CHARSET));
				buf = new byte[0];
				for (Document doc : d.getList("cards", Document.class)) {
					Card card = Card.toCard(doc);
					cards.put(card.getUUID(), card);
				}
			} else
				throw new IOException("Error not file, \"cards.json\" is directory !");
		}
	}
	
	public synchronized void save() throws IOException {
		ArrayList<Document> list = new ArrayList<Document>();
		for (Card card : cards.values())
			list.add(card.toDocument());
		FileOutputStream out = new FileOutputStream(file);
		out.write(new Document("cards", list).toJson().getBytes(Client.CHARSET));
		out.close();
	}
	
	public ConcurrentHashMap<UUID, Card> getCards() {
		return cards;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.CARDS_INSERT)
	public void insert(Client client, Request request) throws IOException, RequestException {
		try {
			cards.put(UUID.fromString(request.name), Card.toCard(Document.parse(request.data)));
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CARDS_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			cards.replace(UUID.fromString(request.name), Card.toCard(Document.parse(request.data)));
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CARDS_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			Card card = cards.get(UUID.fromString(request.name));
			client.sendRequest(request.createReponse(RequestType.CARDS_REPONSE, (card == null) ? "null" : card.toDocument().toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CARDS_GET_ALL)
	public void getAll(Client client, Request request) throws IOException, RequestException {
		try {
			ArrayList<Document> list = new ArrayList<Document>();
			for (Card card : cards.values())
				list.add(card.toDocument());
			client.sendRequest(request.createReponse(RequestType.CARDS_REPONSE, new Document("cards", list).toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CARDS_DELETE)
	public void delete(Client client, Request request) throws IOException, RequestException {
		try {
			cards.remove(UUID.fromString(request.name));
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
