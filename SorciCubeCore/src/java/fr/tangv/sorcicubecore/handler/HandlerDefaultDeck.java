package fr.tangv.sorcicubecore.handler;

import java.io.IOException;

import org.bson.Document;

import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerDefaultDeck {

	private volatile DeckCards deckDark;
	private volatile DeckCards deckLight;
	private volatile DeckCards deckNature;
	private volatile DeckCards deckToxic;
	private final HandlerCards cards;
	private final SorciClient sorci;
	
	public HandlerDefaultDeck(SorciClient sorci, HandlerCards cards) throws IOException, ResponseRequestException, RequestException, DeckException {
		this.sorci = sorci;
		this.cards = cards;
		refresh();
	}
	
	public void refresh() throws IOException, ResponseRequestException, RequestException, DeckException {
		Request reponse = sorci.sendRequestResponse(new Request(RequestType.DEFAULT_DECK_GET, Request.randomID(), "GET", null),
				RequestType.DEFAULT_DECK_REPONSE);
		Document doc = Document.parse(reponse.data);
		this.deckDark = DeckCards.toDeckCards(cards.originalMap(), doc.get("deck_dark", Document.class));
		this.deckLight = DeckCards.toDeckCards(cards.originalMap(), doc.get("deck_light", Document.class));
		this.deckNature = DeckCards.toDeckCards(cards.originalMap(), doc.get("deck_nature", Document.class));
		this.deckToxic = DeckCards.toDeckCards(cards.originalMap(), doc.get("deck_toxic", Document.class));
	}
	
	public void update() throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.DEFAULT_DECK_UPDATE, Request.randomID(), "UPDATE",
				new Document()
				.append("deck_dark", deckDark.toDocument())
				.append("deck_light", deckLight.toDocument())
				.append("deck_nature", deckNature.toDocument())
				.append("deck_toxic", deckToxic.toDocument())
			.toJson()
		),RequestType.SUCCESSFUL);
	}

	public DeckCards getDeckDark() {
		return deckDark;
	}

	public DeckCards getDeckLight() {
		return deckLight;
	}

	public DeckCards getDeckNature() {
		return deckNature;
	}

	public DeckCards getDeckToxic() {
		return deckToxic;
	}
	
}
