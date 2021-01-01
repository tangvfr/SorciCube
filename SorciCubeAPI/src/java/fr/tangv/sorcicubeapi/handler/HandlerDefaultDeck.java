package fr.tangv.sorcicubeapi.handler;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;

public class HandlerDefaultDeck implements RequestHandlerInterface { {

	private DeckCards deckDark;
	private DeckCards deckLight;
	private DeckCards deckNature;
	private DeckCards deckToxic;
	
	public HandlerDefaultDeck() throws Exception {
		/*MongoCollection<Document> defaultDeck = manager.getDefaultDeck();
		Document doc = new Document("default_deck", "default_deck");
		MongoCursor<Document> FDD = defaultDeck.find(doc).iterator();
		if (!FDD.hasNext()) {
			this.deckDark = DeckCards.createDeckCardsEmpty(CardFaction.DARK);
			this.deckLight = DeckCards.createDeckCardsEmpty(CardFaction.LIGHT);
			this.deckNature = DeckCards.createDeckCardsEmpty(CardFaction.NATURE);
			this.deckToxic = DeckCards.createDeckCardsEmpty(CardFaction.TOXIC);
			doc.append("deck_dark", deckDark.toDocument());
			doc.append("deck_light", deckLight.toDocument());
			doc.append("deck_nature", deckNature.toDocument());
			doc.append("deck_toxic", deckToxic.toDocument());
			defaultDeck.insertOne(doc);
		} else {
			doc = FDD.next();
			this.deckDark = DeckCards.toDeckCards(managerCards, doc.get("deck_dark", Document.class));
			this.deckLight = DeckCards.toDeckCards(managerCards, doc.get("deck_light", Document.class));
			this.deckNature = DeckCards.toDeckCards(managerCards, doc.get("deck_nature", Document.class));
			this.deckToxic = DeckCards.toDeckCards(managerCards, doc.get("deck_toxic", Document.class));
		}*/
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
	
	
	
}
