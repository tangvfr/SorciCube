package fr.tangv.sorcicubespell;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

public class TestMongo {

	public static void main(String[] args) {
		String user = "Plugin";
		String password = "r5thihj4k5eh78r1";
		String host = "sorcicube.fr:27017";
		String databaseName = "plugin";
		MongoClient client = MongoClients.create("mongodb://"+user+":"+password+"@"+host+"/?authSource="+databaseName);
		MongoDatabase database = client.getDatabase(databaseName);
		Iterator<String> listCol = database.listCollectionNames().iterator();
		String colCartsName = "carts";
		boolean hasCarts = false;
		while (listCol.hasNext()) {
			String name = listCol.next();
			System.out.println(name);
			if (name.equals(colCartsName)) {
				hasCarts = true;
				break;
			}	
		}
		if (!hasCarts) {
			database.createCollection(colCartsName);
		}
		MongoCollection<Document> carts = database.getCollection(colCartsName);
		
	}
	
}
