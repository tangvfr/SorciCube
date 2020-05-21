package fr.tangv.sorcicubespell.carts;

import java.util.Vector;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class Carts {

	private Vector<? extends Cart> carts;
	
	public Carts(MongoCollection<Document> carts) {
		this.carts = new Vector<Cart>();
		
		//this.carts.add();
	}
	
	
}
