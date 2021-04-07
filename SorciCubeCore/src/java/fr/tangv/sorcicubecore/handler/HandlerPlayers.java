package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
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
	
	public void startUpdatingPlayer(UUID uuid) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_START_UPDATING, Request.randomID(), uuid.toString(), null)
				, RequestType.SUCCESSFUL
			);
	}
	
	public void startSendingPlayer(UUID uuid, String json) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_START_SEND, Request.randomID(), uuid.toString(), json)
				, RequestType.SUCCESSFUL
			);
	}
	
	public boolean existPlayer(UUID uuid) throws IOException, ReponseRequestException, RequestException {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_EXIST, Request.randomID(), uuid.toString(), null),
				RequestType.PLAYER_EXITSTING
			);
		return Boolean.parseBoolean(reponse.data);
	};
	
	public PlayerFeatures getPlayer(UUID uuid, String pseudo) throws IOException, ReponseRequestException, RequestException, DeckException {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_GET, Request.randomID(), uuid.toString(), null),
				RequestType.PLAYER_REPONSE
			);
		return PlayerFeatures.toPlayerFeature(uuid, pseudo, managerCards.originalMap(), Document.parse(reponse.data));
	};
	
	public void update(PlayerFeatures playerFeature) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_UPDATE, Request.randomID(), playerFeature.getUUID().toString(), playerFeature.toDocument().toJson()),
				RequestType.SUCCESSFUL
			);
	};
	
	public PlayerFeatures initPlayer(UUID uuid, String pseudo, CardFaction faction) throws IOException, ReponseRequestException, RequestException, DeckException {
		Request reponse = sorci.sendRequestReponse(
				new Request(RequestType.PLAYER_INIT, Request.randomID(), uuid.toString(), faction.name()+" "+pseudo),
				RequestType.PLAYER_REPONSE
			);
		return PlayerFeatures.toPlayerFeature(uuid, pseudo, managerCards.originalMap(), Document.parse(reponse.data));
	};
	
}
