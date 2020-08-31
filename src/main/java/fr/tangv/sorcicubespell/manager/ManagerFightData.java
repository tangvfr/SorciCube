package fr.tangv.sorcicubespell.manager;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.fight.FightStat;

public class ManagerFightData {

	private MongoCollection<Document> preFightDatas;
	private MongoCollection<Document> spetatorFight;
	
	public ManagerFightData(SorciCubeSpell sorci) {
		this.preFightDatas = sorci.getMongo().getPreFights();
		this.spetatorFight = sorci.getMongo().getSpetatorFight();
	}
	
	public FightData getFightData(UUID uuid) {
		MongoCursor<Document> doc = preFightDatas.find(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString()))).iterator();
		if (doc.hasNext())
			return FightData.toPreFightData(doc.next());
		else
			return null;
	}
	
	public void addFightData(FightData preFightData) {
		preFightDatas.insertOne(preFightData.toDocument());
	}
	
	public void removeFightDataPlayer(UUID uuid) {
		preFightDatas.deleteMany(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString())));
	}
	
	public void removeFightDataUUID(UUID uuid) {
		preFightDatas.deleteMany(new Document("fight_uuid", uuid.toString()));
	}
	
	public boolean changeStatFightDataUUID(UUID uuid, FightStat stat) {
		FightData data = getFightData(uuid);
		if (data == null) return false;
		data.setStat(stat);
		preFightDatas.updateMany(new Document("fight_uuid", uuid.toString()), data.toDocument());
		return true;
	}
	
	public void addFightSpetate(UUID player, UUID fight) {
		Document doc = new Document()
				.append("player", player.toString())
				.append("fight", fight.toString());
		spetatorFight.insertOne(doc);
	}
	
	public UUID whichSpetate(UUID player) {
		Document filter = new Document("player", player.toString());
		MongoCursor<Document> doc = spetatorFight.find(filter).iterator();
		if (doc.hasNext()) {
			spetatorFight.deleteMany(filter);
			return UUID.fromString(doc.next().getString("fight"));
		} else
			return null;
	}
	
}
