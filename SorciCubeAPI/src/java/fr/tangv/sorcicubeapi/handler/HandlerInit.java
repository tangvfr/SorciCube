package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubeapi.server.ClientsManager;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;

public class HandlerInit {

	public HandlerInit(SorciCubeAPI sorci) throws RequestHandlerException, IOException {
		ClientsManager cm = sorci.getClientsManager();
		cm.registered(new HandlerServerPlayers());
		
	}
	
}
