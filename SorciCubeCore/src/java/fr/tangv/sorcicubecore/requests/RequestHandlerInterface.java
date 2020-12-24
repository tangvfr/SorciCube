package fr.tangv.sorcicubecore.requests;

import fr.tangv.sorcicubecore.clients.Client;

public interface RequestHandlerInterface {

	public void handlingRequest(Client client, Request request) throws Exception;
	
}
