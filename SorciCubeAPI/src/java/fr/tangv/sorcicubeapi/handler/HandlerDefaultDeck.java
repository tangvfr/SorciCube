package fr.tangv.sorcicubeapi.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerDefaultDeck implements RequestHandlerInterface {

	private volatile DeckCards deckDark;
	private volatile DeckCards deckLight;
	private volatile DeckCards deckNature;
	private volatile DeckCards deckToxic;
	private final File file;
	private final HandlerServerCards cards;
	
	public HandlerDefaultDeck(HandlerServerCards cards) throws IOException, Exception {
		this.cards = cards;
		this.file = new File("./default_deck.json");
		load();
	}
	
	public synchronized void load() throws IOException, Exception {
		if (!file.exists()) {
			file.createNewFile();
			this.deckDark = DeckCards.createDeckCardsEmpty(CardFaction.DARK);
			this.deckLight = DeckCards.createDeckCardsEmpty(CardFaction.LIGHT);
			this.deckNature = DeckCards.createDeckCardsEmpty(CardFaction.NATURE);
			this.deckToxic = DeckCards.createDeckCardsEmpty(CardFaction.TOXIC);
			save();
		} else {
			if (file.isFile()) {
				FileInputStream in = new FileInputStream(file);
				byte[] buf = new byte[(int) file.length()];
				in.read(buf);
				in.close();
				Document doc = Document.parse(new String(buf, Client.CHARSET));
				buf = new byte[0];
				this.deckDark = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_dark", Document.class));
				this.deckLight = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_light", Document.class));
				this.deckNature = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_nature", Document.class));
				this.deckToxic = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_toxic", Document.class));
			} else
				throw new IOException("Error not file, \"default_deck.json\" is directory !");
		}
	}
	
	public synchronized void save() throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		out.write(new Document()
				.append("deck_dark", deckDark.toDocument())
				.append("deck_light", deckLight.toDocument())
				.append("deck_nature", deckNature.toDocument())
				.append("deck_toxic", deckToxic.toDocument())
			.toJson().getBytes(Client.CHARSET));
		out.close();
	}
	
	public DeckCards getDeckDark() {
		return this.deckDark;
	}
	
	public DeckCards getDeckLight() {
		return this.deckLight;
	}

	public DeckCards getDeckNature() {
		return this.deckNature;
	}
	
	public DeckCards getDeckToxic() {
		return this.deckToxic;
	}

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.DEFAULT_DECK_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.DEFAULT_DECK_REPONSE, new Document()
					.append("deck_dark", deckDark.toDocument())
					.append("deck_light", deckLight.toDocument())
					.append("deck_nature", deckNature.toDocument())
					.append("deck_toxic", deckToxic.toDocument())
				.toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.DEFAULT_DECK_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			Document doc = Document.parse(request.data);
			this.deckDark = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_dark", Document.class));
			this.deckLight = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_light", Document.class));
			this.deckNature = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_nature", Document.class));
			this.deckToxic = DeckCards.toDeckCards(cards.getCards(), doc.get("deck_toxic", Document.class));
			save();
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
