package fr.tangv.sorcicubeapi.requests;

import fr.tangv.sorcicubeapi.clients.Client;

public interface RequestHandlerInterface {

	public void handlingRequest(Client client, Request request) throws Exception;
	
}
