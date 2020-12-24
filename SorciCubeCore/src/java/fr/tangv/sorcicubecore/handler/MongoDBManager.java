package fr.tangv.sorcicubecore.handler;

import java.util.Collection;

import org.bson.Document;

public class MongoDBManager {

	/*private Iterable<String> listCol;
	private Object database;*/
	private Collection<Document> cards;
	private Collection<Document> players;
	private Collection<Document> defaultDeck;
	private Collection<Document> packets;
	private Collection<Document> preFights;
	private Collection<Document> spetatorFight;
	
	public MongoDBManager(String uri, String collectionName) throws Exception {
		//init
		/*MongoClient client = MongoClients.create(uri);
		this.database = client.getDatabase(collectionName);
		this.listCol = database.listCollectionNames();*/
		//init collection
		this.cards = defineCollection("cards");
		this.players = defineCollection("players");
		this.defaultDeck = defineCollection("default_deck");
		this.packets = defineCollection("packets");
		this.preFights = defineCollection("pre_fights");
		this.spetatorFight = defineCollection("spetator_fight");
	}
	
	private Collection<Document> defineCollection(String collection) {
		/*boolean hasCollection = false;
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
		return database.getCollection(collection);*/
		return null;
	}

	protected Collection<Document> getCards() {
		return cards;
	}
	
	protected Collection<Document> getPlayers() {
		return players;
	}
	
	protected Collection<Document> getDefaultDeck() {
		return defaultDeck;
	}
	
	protected Collection<Document> getPackets() {
		return packets;
	}
	
	protected Collection<Document> getPreFights() {
		return preFights;
	}
	
	protected Collection<Document> getSpetatorFight() {
		return spetatorFight;
	}

}
