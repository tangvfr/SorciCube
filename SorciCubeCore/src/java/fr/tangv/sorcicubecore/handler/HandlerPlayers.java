package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerPlayers {

	private final SorciClient sorci;
	private final HandlerCards managerCards;
	
	
	public HandlerPlayers(SorciClient sorci, HandlerCards managerCards) {
		this.sorci = sorci;
		this.managerCards = managerCards;
	}
	
	public boolean containtPlayer(UUID uuid) throws IOException, ReponseRequestException, RequestException {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_EXIST, Request.randomID(), uuid.toString(), null),
				RequestType.PLAYER_EXITSTING
			);
		return Boolean.parseBoolean(reponse.data);
	};
	
	public PlayerFeature getPlayer(UUID uuid) throws IOException, ReponseRequestException, RequestException, Exception {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_GET, Request.randomID(), uuid.toString(), null),
				RequestType.PLAYER_REPONSE
			);
		return PlayerFeature.toPlayerFeature(uuid, managerCards.originalCards(), Document.parse(reponse.data));
	};
	
	public void update(PlayerFeature playerFeature) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_UPDATE, Request.randomID(), playerFeature.getUUID().toString(), playerFeature.toDocument().toJson()),
				RequestType.SUCCESSFUL
			);
	};
	
	public PlayerFeature initPlayer(UUID uuid, CardFaction faction) throws IOException, ReponseRequestException, RequestException, Exception {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_INIT, Request.randomID(), uuid.toString(), faction.name()),
				RequestType.PLAYER_REPONSE
			);
		return PlayerFeature.toPlayerFeature(uuid, managerCards.originalCards(), Document.parse(reponse.data));
	};
	
}
