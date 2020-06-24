package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.card.Card;

public class ManagerCards {

	private MongoCollection<Document> cards;
	
	public ManagerCards(MongoDBManager manager) {
		this.cards = manager.getCards();
	}
	
	public HashMap<UUID, Card> getCarts() {
		HashMap<UUID, Card> map = new HashMap<UUID, Card>();
		for (Document doc : cards.find()) {
			Card card = Card.toCard(doc);
			map.put(card.getUUID(), card);
		}
		return map;
	}
	
	public Card getCart(UUID uuid) {
		Iterator<Document> rep = cards.find(Card.toUUIDDocument(uuid)).iterator();
		if (rep.hasNext())
			return Card.toCard(rep.next());
		else
			return null;
	}
	
	public void insert(Card cart) {
		cards.insertOne(cart.toDocument());
	}
	
	public void update(Card cart) {
		cards.findOneAndReplace(cart.toUUIDDocument(), cart.toDocument());
	}
	
	public void delete(Card cart) {
		cards.findOneAndDelete(cart.toUUIDDocument());
	}
	
}
