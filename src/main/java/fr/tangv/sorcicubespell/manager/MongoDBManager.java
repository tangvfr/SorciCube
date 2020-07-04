package fr.tangv.sorcicubespell.manager;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoDBManager {

	private MongoIterable<String> listCol;
	private MongoDatabase database;
	private MongoCollection<Document> cards;
	private MongoCollection<Document> players;
	private MongoCollection<Document> defaultDeck;
	private MongoCollection<Document> packets;
	private MongoCollection<Document> preFights;
	private String uri;
	private String databaseName;
	
	public MongoDBManager(String uri, String database) throws Exception {
		this.uri = uri;
		this.databaseName = database;
		this.refrech();
	}
	
	private MongoCollection<Document> defineCollection(String collection) {
		boolean hasCollection = false;
		MongoCursor<String> it = listCol.iterator();
		while (it.hasNext()) {
			String name = it.next();
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
	
	public MongoCollection<Document> getPreFights() {
		return preFights;
	}
	
	public void refrech() {
		//init
		MongoClient client = MongoClients.create(uri);
		this.database = client.getDatabase(databaseName);
		this.listCol = database.listCollectionNames();
		//init collection
		this.cards = defineCollection("cards");
		this.players = defineCollection("players");
		this.defaultDeck = defineCollection("default_deck");
		this.packets = defineCollection("packets");
		this.preFights = defineCollection("pre_fights");
	}
	
}
