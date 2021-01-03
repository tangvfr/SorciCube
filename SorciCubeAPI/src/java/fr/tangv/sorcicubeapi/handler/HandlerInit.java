package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubeapi.server.ClientsManager;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;

public class HandlerInit {

	public HandlerInit(SorciCubeAPI sorci) throws RequestHandlerException, IOException, DeckException {
		HandlerServerConfig config = new HandlerServerConfig();
		//handler
		HandlerServerCards cards = new HandlerServerCards();
		HandlerServerDefaultDeck defaultDeck = new HandlerServerDefaultDeck(cards);
		HandlerServerFightData fightData = new HandlerServerFightData();
		HandlerServerPlayers players = new HandlerServerPlayers(config, cards, defaultDeck);
		//registred
		ClientsManager cm = sorci.getClientsManager();
		cm.registered(cards);
		cm.registered(defaultDeck);
		cm.registered(fightData);
		cm.registered(players);
	}
	
}
