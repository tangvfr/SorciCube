package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubeapi.console.Console;
import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientType;
import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.ramfiles.RamFilesManager;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerPlayers implements RequestHandlerInterface {

	private final RamFilesManager fm;
	private final HandlerServerDefaultDeck defaultDeck;
	private final SorciCubeAPI sorci;
	
	public HandlerServerPlayers(HandlerServerDefaultDeck defaultDeck, SorciCubeAPI sorci) throws IOException {
		this.fm = new RamFilesManager("./players");
		this.defaultDeck = defaultDeck;
		this.sorci = sorci;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.PLAYER_START_UPDATING)
	public void startUpdating(Client client, Request request) throws RequestException, IOException {
		Request updating = new Request(RequestType.PLAYER_UPDATING, Request.randomID(), request.name, null);
		for (Client cl : sorci.getClientsManager().clients)
			if (ClientType.SPIGOT.isType(cl.getClientID().types))
				cl.sendRequest(updating);
		client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_START_SEND)
	public void startSendding(Client client, Request request) throws RequestException, IOException {
		Request updating = new Request(RequestType.PLAYER_SEND, Request.randomID(), request.name, request.data);
		for (Client cl : sorci.getClientsManager().clients)
			if (ClientType.SPIGOT.isType(cl.getClientID().types))
				cl.sendRequest(updating);
		client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_INIT)
	public void init(Client client, Request request) throws IOException, RequestException {
		try {
			UUID player = UUID.fromString(request.name);
			String args[] = request.data.split(" ");
			CardFaction faction = CardFaction.valueOf(args[0]);
			String pseudo = args[1];
			if (fm.has(player.toString()))
				throw new Exception("Player already registred !");
			//define type default deck
			DeckCards defaultDeck;
			switch (faction) {
				case DARK:
					defaultDeck = this.defaultDeck.getDeckDark();
					break;
					
				case LIGHT:
					defaultDeck = this.defaultDeck.getDeckLight();
					break;
					
				case NATURE:
					defaultDeck = this.defaultDeck.getDeckNature();
					break;
					
				case TOXIC:
					defaultDeck = this.defaultDeck.getDeckToxic();
					break;
	
				default:
					throw new Exception("Faction for default deck is invalid !");
			}
			if (!defaultDeck.isComplet())
				throw new Exception("Default deck is not complet !");
			//list cards unlocked
			ArrayList<String> cardsUnlocks = new ArrayList<String>();
			for (int i = 0; i < defaultDeck.size(); i++) {
				Card card = defaultDeck.getCard(i);
				if (card != null)
					cardsUnlocks.add(card.getUUID().toString());
			}
			//defaultGroup
			
			//create and insert playerfeature
			PlayerFeature playerFeature = new PlayerFeature(player,
					pseudo,
					defaultDeck,
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					2,
					cardsUnlocks,
					new ArrayList<String>(),
					0, 0, (byte) 1, defaultGroup, false);
			String playerJson = playerFeature.toDocument().toJson();
			if (playerJson == null || playerJson.isEmpty())
				throw new Exception("Echec PlayerFeature to Json !");
			fm.insert(player.toString(), playerJson);
			client.sendRequest(request.createReponse(RequestType.PLAYER_REPONSE, playerJson));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
			Console.logger.throwing("HandlerServerPlayers", "init", e);
			e.printStackTrace();
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			fm.update(request.name, request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.PLAYER_REPONSE, fm.get(request.name).getData()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}

	@RequestAnnotation(type=RequestType.PLAYER_EXIST)
	public void exist(Client client, Request request) throws IOException, RequestException {
		client.sendRequest(request.createReponse(RequestType.PLAYER_EXITSTING, Boolean.toString(fm.has(request.name))));
	}
	
}
