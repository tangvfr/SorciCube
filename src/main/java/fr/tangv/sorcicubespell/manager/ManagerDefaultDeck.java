package fr.tangv.sorcicubespell.manager;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import fr.tangv.sorcicubespell.player.DeckCards;

public class ManagerDefaultDeck {

	private DeckCards deckDark;
	private DeckCards deckLight;
	private DeckCards deckNature;
	private DeckCards deckToxic;
	
	public ManagerDefaultDeck(MongoDBManager manager, ManagerCards managerCards) throws Exception {
		MongoCollection<Document> defaultDeck = manager.getDefaultDeck();
		Document doc = new Document("default_deck", "default_deck");
		MongoCursor<Document> FDD = defaultDeck.find(doc).iterator();
		if (!FDD.hasNext()) {
			this.deckDark = DeckCards.createDeckCarsEmpty();
			this.deckLight = DeckCards.createDeckCarsEmpty();
			this.deckNature = DeckCards.createDeckCarsEmpty();
			this.deckToxic = DeckCards.createDeckCarsEmpty();
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
		}
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
	
}
