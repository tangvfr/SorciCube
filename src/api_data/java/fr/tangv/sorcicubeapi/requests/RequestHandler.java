package fr.tangv.sorcicubeapi.requests;

import java.lang.reflect.Method;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubeapi.clients.Client;

public class RequestHandler implements RequestHandlerInterface {

	private Vector<RequestHandlerInterface> requestHandlers;
	private ConcurrentHashMap<RequestType, Vector<Method>> requestHandlersList;
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		
	}
	
}
