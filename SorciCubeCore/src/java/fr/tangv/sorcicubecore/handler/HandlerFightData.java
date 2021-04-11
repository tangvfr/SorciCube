package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.bson.Document;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerFightData {

	private final SorciClient sorci;
	
	public HandlerFightData(SorciClient sorci) {
		this.sorci = sorci;
	}
	
	public FightData getFightDataPlayer(UUID player) throws IOException, ResponseRequestException, RequestException {
		Request reponse = sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_PLAYER_GET, Request.randomID(), player.toString(), null),
				RequestType.FIGHT_DATA_ONE);
		if (reponse.data.equals("null"))
			return null;
		return FightData.toFightData(Document.parse(reponse.data));
	}
	
	public void removeFightDataPlayer(UUID uuid) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_PLAYER_REMOVE, Request.randomID(), uuid.toString(), null),
				RequestType.SUCCESSFUL);
	}
	
	public Vector<FightData> getAllFightData() throws IOException, ResponseRequestException, RequestException {
		Request reponse = sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_GET_LIST, Request.randomID(), "ListFightData", null),
				RequestType.FIGHT_DATA_LIST);
		Vector<FightData> list = new Vector<FightData>();
		for (Document doc : Document.parse(reponse.data).getList("list", Document.class))
			list.add(FightData.toFightData(doc));
		return list;
	}
	
	public void addFightData(FightData fightData) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_ADD, Request.randomID(), fightData.getFightUUID().toString(), fightData.toDocument().toJson()),
				RequestType.SUCCESSFUL);
	}
	
	public void removeFightDataFight(FightData fightData) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_REMOVE, Request.randomID(), fightData.getFightUUID().toString(), null),
				RequestType.SUCCESSFUL);
	}
	
	public void updateFightData(FightData fightData) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_UPDATE, Request.randomID(), fightData.getFightUUID().toString(), fightData.toDocument().toJson()),
				RequestType.SUCCESSFUL);
	}
	
	public void removeAllFightDataServer(String server) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.FIGHT_DATA_SERVER_REMOVE, Request.randomID(), server, null),
				RequestType.SUCCESSFUL);
	}
	
	public void addFightSpectate(UUID player, UUID fight) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(new Request(RequestType.SPECTATOR_ADD, Request.randomID(), player.toString(), fight.toString()),
				RequestType.SUCCESSFUL);
	}
	
	public UUID whichSpectate(UUID player) throws IOException, ResponseRequestException, RequestException {
		Request reponse = sorci.sendRequestResponse(new Request(RequestType.SPECTATOR_PEEK, Request.randomID(), player.toString(), null),
				RequestType.SPECTATOR_UUID);
		if (reponse.data.equals("null"))
			return null;
		return UUID.fromString(reponse.data);
	}
	
}
