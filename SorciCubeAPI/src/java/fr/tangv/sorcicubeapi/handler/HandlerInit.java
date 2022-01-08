package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubeapi.console.Console;
import fr.tangv.sorcicubeapi.server.ClientsManager;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerInit {

	public HandlerInit(SorciCubeAPI sorci) throws RequestHandlerException, IOException, DeckException, ConfigParseException {
		//handler
		HandlerServerCards cards = new HandlerServerCards();
		HandlerServerGroups groups = new HandlerServerGroups();
		HandlerServerDefaultDeck defaultDeck = new HandlerServerDefaultDeck(cards);
		HandlerServerFightData fightData = new HandlerServerFightData();
		HandlerServerConfig config = new HandlerServerConfig();
		HandlerServerPlayers players = new HandlerServerPlayers(defaultDeck, groups, config, sorci);
		HandlerServerPacketCards packets = new HandlerServerPacketCards();
		HandlerServerServer server = new HandlerServerServer(sorci);
		//registred
		ClientsManager cm = sorci.getClientsManager();
		cm.registered(cards);
		cm.registered(groups);
		cm.registered(defaultDeck);
		cm.registered(fightData);
		cm.registered(players);
		cm.registered(config);
		cm.registered(packets);
		cm.registered(server);
		cm.registered(new RequestHandlerInterface() {
			@Override
			public void handlingRequest(Client client, Request request) throws Exception {
				if (request.requestType != RequestType.FIGHT_DATA_GET_LIST)
					Console.logger.info(client.getInetAddress().getHostAddress()+":"+client.getClientID().name+" <recieve< "+request.toRequestNoData());
			}
		});
	}
	
}
