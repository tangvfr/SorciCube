package fr.tangv.sorcicubecore.handler;

import java.io.IOException;

import org.bson.Document;

import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerDefaultDeck {

	private volatile DeckCards deckDark;
	private volatile DeckCards deckLight;
	private volatile DeckCards deckNature;
	private volatile DeckCards deckToxic;
	private final HandlerCards cards;
	private final SorciClient sorci;
	
	public HandlerDefaultDeck(SorciClient sorci, HandlerCards cards) throws IOException, ReponseRequestException, RequestException, DeckException {
		this.sorci = sorci;
		this.cards = cards;
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException, DeckException {
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.DEFAULT_DECK_GET, Request.randomID(), "GET", null),
				RequestType.DEFAULT_DECK_REPONSE);
		Document doc = Document.parse(reponse.data);
		this.deckDark = DeckCards.toDeckCards(cards.originalCards(), doc.get("deck_dark", Document.class));
		this.deckLight = DeckCards.toDeckCards(cards.originalCards(), doc.get("deck_light", Document.class));
		this.deckNature = DeckCards.toDeckCards(cards.originalCards(), doc.get("deck_nature", Document.class));
		this.deckToxic = DeckCards.toDeckCards(cards.originalCards(), doc.get("deck_toxic", Document.class));
	}
	
}
