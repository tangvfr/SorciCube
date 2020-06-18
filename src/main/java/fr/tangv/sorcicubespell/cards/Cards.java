package fr.tangv.sorcicubespell.cards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.manager.MongoDBManager;

public class Cards {

	private MongoCollection<Document> carts;
	
	public Cards(MongoDBManager manager) {
		this.carts = manager.getCarts();
	}
	
	public ArrayList<Card> getCarts() {
		ArrayList<Card> list = new ArrayList<Card>();
		for(Document doc : carts.find()) {
			Card cart = Card.toCart(doc);
			list.add(cart);
		}
		return list;
	}
	
	public Card getCart(UUID uuid) {
		Iterator<Document> rep = carts.find(Card.toUUIDDocument(uuid)).iterator();
		if (rep.hasNext())
			return Card.toCart(rep.next());
		else
			return null;
	}
	
	public void insert(Card cart) {
		carts.insertOne(cart.toDocument());
	}
	
	public void update(Card cart) {
		carts.findOneAndReplace(cart.toUUIDDocument(), cart.toDocument());
	}
	
	public void delete(Card cart) {
		carts.findOneAndDelete(cart.toUUIDDocument());
	}
	
}
