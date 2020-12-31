package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	private int manyStartDecks;
	
	public HandlerServerPlayers(int manyStartDecks) throws IOException {
		this.fm = new RamFilesManager("./players");
		this.manyStartDecks = manyStartDecks;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@Deprecated
	@RequestAnnotation(type=RequestType.PLAYER_INIT)
	public void init(Client client, Request request) throws IOException, RequestException {
		try {
			Document doc = Document.parse(request.data);
			String player = request.name;
			CardFaction faction = CardFaction.valueOf(doc.getString("faction"));
			if (fm.has(player))
				throw new Exception("Player already registred !");
			//define type default deck
			DeckCards defaultDeck = null;//remove this
			switch (faction) {
				case DARK:
					//defaultDeck = sorci.getManagerDefaultDeck().getDeckDark();
					break;
					
				case LIGHT:
					//defaultDeck = sorci.getManagerDefaultDeck().getDeckLight();
					break;
					
				case NATURE:
					//defaultDeck = sorci.getManagerDefaultDeck().getDeckNature();
					break;
					
				case TOXIC:
					//defaultDeck = sorci.getManagerDefaultDeck().getDeckToxic();
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
			PlayerFeature playerFeature = new PlayerFeature(UUID.fromString(player),
					defaultDeck,
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					this.manyStartDecks,
					cardsUnlocks,
					new ArrayList<String>(),
					0, 0, (byte) 1);
			fm.insert(request.name, playerFeature.toDocument().toJson());
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			fm.update(request.name, request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (IOException e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.PLAYER_REPONSE, fm.get(request.name).getData()));
		} catch (IOException e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}

	@RequestAnnotation(type=RequestType.PLAYER_EXIST)
	public void exist(Client client, Request request) throws IOException, RequestException {
		client.sendRequest(request.createReponse(RequestType.PLAYER_EXITSTED, Boolean.toString(fm.has(request.name))));
	}
	
}
