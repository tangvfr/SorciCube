package fr.tangv.sorcicubespell.manager;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.PreFightData;

public class ManagerPreFightData {

	private MongoCollection<Document> preFightDatas;
	
	public ManagerPreFightData(SorciCubeSpell sorci) {
		this.preFightDatas = sorci.getMongo().getPreFights();
	}
	
	public PreFightData getPreFightData(UUID uuid) {
		MongoCursor<Document> doc = preFightDatas.find(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString()))).iterator();
		if (doc.hasNext())
			return PreFightData.toPreFightData(doc.next());
		else
			return null;
	}
	
	public void addPreFightData(PreFightData preFightData) {
		preFightDatas.insertOne(preFightData.toDocument());
	}
	
	public void removePreFightData(UUID uuid) {
		preFightDatas.deleteMany(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString())));
	}
	
}
