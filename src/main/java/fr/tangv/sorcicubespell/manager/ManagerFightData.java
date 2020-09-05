package fr.tangv.sorcicubespell.manager;

import java.util.UUID;
import java.util.Vector;

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
	
	public Vector<FightData> getAllFightData() {
		Vector<FightData> list = new Vector<FightData>();
		MongoCursor<Document> listFight = preFightDatas.find().iterator();
		while (listFight.hasNext())
			list.add(FightData.toFightData(listFight.next()));
		return list;
	}
	
	public FightData getFightDataPlayer(UUID player) {
		MongoCursor<Document> doc = preFightDatas.find(Filters.or(new Document("player1", player.toString()), new Document("player2", player.toString()))).iterator();
		if (doc.hasNext())
			return FightData.toFightData(doc.next());
		else
			return null;
	}
	
	public FightData getFightDataFight(UUID fight) {
		MongoCursor<Document> doc = preFightDatas.find(new Document("uuid", fight.toString())).iterator();
		if (doc.hasNext())
			return FightData.toFightData(doc.next());
		else
			return null;
	}
	
	public void addFightData(FightData preFightData) {
		preFightDatas.insertOne(preFightData.toDocument());
	}
	
	public void removeFightDataPlayer(UUID uuid) {
		preFightDatas.deleteMany(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString())));
	}
	
	public void removeFightDataFight(UUID uuid) {
		preFightDatas.deleteMany(new Document("uuid", uuid.toString()));
	}
	
	public boolean changeStatFightDataFight(UUID fight, FightStat stat) {
		FightData data = getFightDataFight(fight);
		if (data == null) return false;
		data.setStat(stat);
		preFightDatas.updateMany(new Document("uuid", fight.toString()), data.toDocument());
		return true;
	}
	
	public void removeAllFightData() {
		preFightDatas.deleteMany(new Document());
	}
	
	public void addFightSpectate(UUID player, UUID fight) {
		Document doc = new Document()
				.append("player", player.toString())
				.append("fight", fight.toString());
		spetatorFight.insertOne(doc);
	}
	
	public UUID whichSpectate(UUID player) {
		Document filter = new Document("player", player.toString());
		MongoCursor<Document> doc = spetatorFight.find(filter).iterator();
		if (doc.hasNext()) {
			spetatorFight.deleteMany(filter);
			return UUID.fromString(doc.next().getString("fight"));
		} else
			return null;
	}
	
}
