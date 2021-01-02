package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class ManagerFightData {

	private final SorciClient sorci;
	
	public ManagerFightData(SorciClient sorci) {
		this.sorci = sorci;
	}
	
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
	
	public void addFightSpectate(UUID player, UUID fight) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.SPECTATOR_ADD, Request.randomID(), player.toString(), fight.toString()),
				RequestType.SUCCESSFUL);
	}
	
	public UUID whichSpectate(UUID player) throws IOException, ReponseRequestException, RequestException {
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.SPECTATOR_PEEK, Request.randomID(), player.toString(), null),
				RequestType.SPECTATOR_UUID);
		if (reponse.data.equals("null"))
			return null;
		return UUID.fromString(reponse.data);
	}
	
}
