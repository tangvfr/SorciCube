package fr.tangv.sorcicubespell.manager;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.card.Card;

public class ManagerCards {

	private MongoCollection<Document> cardsCol;
	private ConcurrentHashMap<UUID, Card> cards;
	
	public ManagerCards(MongoDBManager manager) {
		this.cardsCol = manager.getCards();
		refrech();
	}
	
	public void refrech() {
		this.cards = new ConcurrentHashMap<UUID, Card>();
		for (Document doc : cardsCol.find()) {
			Card card = Card.toCard(doc);
			cards.put(card.getUUID(), card);
		}
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
	
	public void insert(Card card) {
		cardsCol.insertOne(card.toDocument());
	}
	
	public void update(Card card) {
		cardsCol.findOneAndReplace(card.toUUIDDocument(), card.toDocument());
	}
	
	public void delete(Card card) {
		cardsCol.findOneAndDelete(card.toUUIDDocument());
	}
	
}
