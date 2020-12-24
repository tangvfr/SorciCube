package fr.tangv.sorcicubecore.handler;

import java.util.UUID;
import java.util.Vector;

import fr.tangv.sorcicubecore.fight.FightData;

public class ManagerFightData {

	/*private MongoCollection<Document> preFightDatas;
	private MongoCollection<Document> spetatorFight;
	
	public ManagerFightData(MongoDBManager manager) {
		this.preFightDatas = manager.getPreFights();
		this.spetatorFight = manager.getSpetatorFight();
	}*/
	
	public Vector<FightData> getAllFightData() {
		/*Vector<FightData> list = new Vector<FightData>();
		MongoCursor<Document> listFight = preFightDatas.find().iterator();
		while (listFight.hasNext())
			list.add(FightData.toFightData(listFight.next()));
		return list;*/return null;
	}
	
	public FightData getFightDataPlayer(UUID player) {
		/*MongoCursor<Document> doc = preFightDatas.find(Filters.or(new Document("player1", player.toString()), new Document("player2", player.toString()))).iterator();
		if (doc.hasNext())
			return FightData.toFightData(doc.next());
		else*/
			return null;
	}
	
	public void addFightData(FightData fightData) {
		//preFightDatas.insertOne(fightData.toDocument());
	}
	
	public void removeFightDataPlayer(UUID uuid) {
		//preFightDatas.deleteMany(Filters.or(new Document("player1", uuid.toString()), new Document("player2", uuid.toString())));
	}
	
	public void removeFightDataFight(FightData fightData) {
		//preFightDatas.deleteMany(fightData.toDocumentID());
	}
	
	public void updateFightData(FightData fightData) {
		removeFightDataFight(fightData);
		addFightData(fightData);
	}
	
	public void removeAllFightData() {
		//preFightDatas.deleteMany(new Document());
	}
	
	public void addFightSpectate(UUID player, UUID fight) {
		/*Document doc = new Document()
				.append("player", player.toString())
				.append("fight", fight.toString());
		spetatorFight.insertOne(doc);*/
	}
	
	public UUID whichSpectate(UUID player) {
		/*Document filter = new Document("player", player.toString());
		MongoCursor<Document> doc = spetatorFight.find(filter).iterator();
		if (doc.hasNext()) {
			spetatorFight.deleteMany(filter);
			return UUID.fromString(doc.next().getString("fight"));
		} else*/
			return null;
	}
	
}
