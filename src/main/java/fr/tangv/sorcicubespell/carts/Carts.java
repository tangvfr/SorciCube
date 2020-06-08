package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.manager.MongoDBManager;

public class Carts {

	private MongoCollection<Document> carts;
	
	public Carts(MongoDBManager manager) {
		this.carts = manager.getCarts();
	}
	
	public ArrayList<Cart> getCarts() {
		ArrayList<Cart> list = new ArrayList<Cart>();
		for(Document doc : carts.find()) {
			Cart cart = Cart.toCart(doc);
			list.add(cart);
		}
		return list;
	}
	
	public Cart getCart(UUID uuid) {
		Iterator<Document> rep = carts.find(Cart.toUUIDDocument(uuid)).iterator();
		if (rep.hasNext())
			return Cart.toCart(rep.next());
		else
			return null;
	}
	
	public void insert(Cart cart) {
		carts.insertOne(cart.toDocument());
	}
	
	public void update(Cart cart) {
		carts.findOneAndReplace(cart.toUUIDDocument(), cart.toDocument());
	}
	
	public void delete(Cart cart) {
		carts.findOneAndDelete(cart.toUUIDDocument());
	}
	
}
