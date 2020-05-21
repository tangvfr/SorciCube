package fr.tangv.sorcicubespell.carts;

import java.util.Vector;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class Carts {

	private MongoCollection<Document> cartsCol;
	private Vector<? extends Cart> carts;
	
	public Carts(MongoCollection<Document> cartsCol) {
		this.carts = new Vector<Cart>();
		this.cartsCol = cartsCol;
		for(Document doc : cartsCol.find()) {
			System.out.println(doc.getObjectId("_id").toString());
		}
	}
	
	public void insert(Cart cart) {
		
	}
	
	public void update(Cart cart) {
		
	}
	
	public void delete(Cart cart) {
		
	}
	
}
