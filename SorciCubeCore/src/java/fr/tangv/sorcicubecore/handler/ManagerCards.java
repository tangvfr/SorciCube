package fr.tangv.sorcicubecore.handler;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.card.Card;

public class ManagerCards {

	//private MongoCollection<Document> cardsCol;
	private final ConcurrentHashMap<UUID, Card> cards;
	
	public ManagerCards(MongoDBManager mongo) {
		this.cards = new ConcurrentHashMap<UUID, Card>();
		//this.cardsCol = manager.getCards();
		refresh();
	}
	
	public void refresh() {
		/*this.cards.clear();
		for (Document doc : cardsCol.find()) {
			Card card = Card.toCard(doc);
			cards.put(card.getUUID(), card);
		}*/
	}
	
	public ConcurrentHashMap<UUID, Card> originalCards() {
		return cards;
	}
	
	public ConcurrentHashMap<UUID, Card> cloneCards() {
		return new ConcurrentHashMap<UUID, Card>(cards);
	}
	
	public Vector<Card> cloneCardsValue() {
		return new Vector<Card>(cards.values());
	}
	
	public Card getCard(UUID uuid) {
		if (cards.containsKey(uuid))
			return cards.get(uuid);
		else
			return null;
	}
	
	public boolean insert(Card card) {
		return false;
		//cardsCol.insertOne(card.toDocument());
	}
	
	public boolean update(Card card) {
		return false;
		//cardsCol.findOneAndReplace(card.toUUIDDocument(), card.toDocument());
	}
	
	public boolean delete(Card card) {
		return false;
		//cardsCol.findOneAndDelete(card.toUUIDDocument());
	}
	
}
