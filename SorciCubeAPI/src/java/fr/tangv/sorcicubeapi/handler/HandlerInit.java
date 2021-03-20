package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubeapi.console.Console;
import fr.tangv.sorcicubeapi.server.ClientsManager;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerInit {

	public HandlerInit(SorciCubeAPI sorci) throws RequestHandlerException, IOException, DeckException {
		//handler
		HandlerServerCards cards = new HandlerServerCards();
		HandlerServerDefaultDeck defaultDeck = new HandlerServerDefaultDeck(cards);
		HandlerServerFightData fightData = new HandlerServerFightData();
		HandlerServerPlayers players = new HandlerServerPlayers(defaultDeck, sorci);
		HandlerServerConfigYAML yaml = new HandlerServerConfigYAML();
		HandlerServerPacketCards packets = new HandlerServerPacketCards();
		//registred
		ClientsManager cm = sorci.getClientsManager();
		cm.registered(cards);
		cm.registered(defaultDeck);
		cm.registered(fightData);
		cm.registered(players);
		cm.registered(yaml);
		cm.registered(packets);
		cm.registered(new RequestHandlerInterface() {
			@Override
			public void handlingRequest(Client client, Request request) throws Exception {
				if (request.requestType != RequestType.FIGHT_DATA_GET_LIST)
					Console.logger.info(client.getInetAddress().getHostAddress()+":"+client.getClientID().name+" <recieve< "+request.toRequestNoData());
			}
		});
	}
	
}
