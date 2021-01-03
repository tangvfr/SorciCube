package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.clients.Client;
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
	private final HandlerServerConfig config;
	private final HandlerServerCards cards;
	private final HandlerServerDefaultDeck defaultDeck;
	
	public HandlerServerPlayers(HandlerServerConfig config, HandlerServerCards cards, HandlerServerDefaultDeck defaultDeck) throws IOException {
		this.fm = new RamFilesManager("./players");
		this.config = config;
		this.cards = cards;
		this.defaultDeck = defaultDeck;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@Deprecated
	@RequestAnnotation(type=RequestType.PLAYER_INIT)
	public void init(Client client, Request request) throws IOException, RequestException {
		try {
			UUID player = UUID.fromString(request.name);
			CardFaction faction = CardFaction.valueOf(request.data);
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
			//list cards unlocked
			ArrayList<String> cardsUnlocks = new ArrayList<String>();
			for (int i = 0; i < defaultDeck.size(); i++) {
				Card card = defaultDeck.getCard(i);
				if (card != null)
					cardsUnlocks.add(card.getUUID().toString());
			}
			//create and insert playerfeature
			PlayerFeature playerFeature = new PlayerFeature(player,
					defaultDeck,
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					this.config.getManyStartDecks(),
					cardsUnlocks,
					new ArrayList<String>(),
					0, 0, (byte) 1);
			String playerJson = playerFeature.toDocument().toJson();
			fm.insert(player.toString(), playerJson);
			client.sendRequest(request.createReponse(RequestType.PLAYER_REPONSE, playerJson));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			PlayerFeature.toPlayerFeature(UUID.fromString(request.name), cards.getCards(), Document.parse(request.data));
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
