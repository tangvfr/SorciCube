package fr.tangv.sorcicubespell.manager;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBManager {

	private MongoDatabase database;
	private MongoCollection<Document> cards;
	private MongoCollection<Document> players;
	private MongoCollection<Document> defaultDeck;
	private MongoCollection<Document> packets;
	private String uri;
	private String databaseName;
	
	public MongoDBManager(String uri, String database) throws Exception {
		this.uri = uri;
		this.databaseName = database;
		this.refrech();
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

	public MongoCollection<Document> getCards() {
		return cards;
	}
	
	public MongoCollection<Document> getPlayers() {
		return players;
	}
	
	public MongoCollection<Document> getDefaultDeck() {
		return defaultDeck;
	}
	
	public MongoCollection<Document> getPackets() {
		return packets;
	}
	
	public void refrech() {
		//init
		MongoClient client = MongoClients.create(uri);
		this.database = client.getDatabase(databaseName);
		//init collection
		this.cards = defineCollection("cards");
		this.players = defineCollection("players");
		this.defaultDeck = defineCollection("default_deck");
		this.packets = defineCollection("packets");
	}
	
}
