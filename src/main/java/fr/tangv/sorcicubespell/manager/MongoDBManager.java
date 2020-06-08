package fr.tangv.sorcicubespell.manager;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBManager {

	private MongoDatabase database;
	private MongoCollection<Document> carts;
	
	public MongoDBManager(String url, String database) {
		//init
		MongoClient client = MongoClients.create(url);
		this.database = client.getDatabase(database);
		//init collection
		this.carts = defineCollection("carts");
	}
	
	private MongoCollection<Document> defineCollection(String collection) {
		Iterator<String> listCol = database.listCollectionNames().iterator();
		boolean hasCollection = false;
		while (listCol.hasNext()) {
			String name = listCol.next();
			if (name.equals(collection)) {
				hasCollection = true;
				break;
			}	
		}
		if (!hasCollection)
			database.createCollection(collection);
		return database.getCollection(collection);
	}

	public MongoCollection<Document> getCarts() {
		return carts;
	}
	
}
