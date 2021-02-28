package fr.tangv.sorcicubeapp;

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
	private MongoCollection<Document> spetatorFight;
	
	public MongoDBManager(String uri, String collectionName) throws Exception {
		//init
		MongoClient client = MongoClients.create(uri);
		this.database = client.getDatabase(collectionName);
		this.listCol = database.listCollectionNames();
		//init collection
		this.cards = defineCollection("cards");
		this.players = defineCollection("players");
		this.defaultDeck = defineCollection("default_deck");
		this.packets = defineCollection("packets");
		this.preFights = defineCollection("pre_fights");
		this.spetatorFight = defineCollection("spetator_fight");
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

	protected MongoCollection<Document> getCards() {
		return cards;
	}
	
	protected MongoCollection<Document> getPlayers() {
		return players;
	}
	
	protected MongoCollection<Document> getDefaultDeck() {
		return defaultDeck;
	}
	
	protected MongoCollection<Document> getPackets() {
		return packets;
	}
	
	protected MongoCollection<Document> getPreFights() {
		return preFights;
	}
	
	protected MongoCollection<Document> getSpetatorFight() {
		return spetatorFight;
	}

}
