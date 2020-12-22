package fr.tangv.sorcicubeapi.requests;

import java.lang.reflect.Method;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubeapi.clients.Client;

public class RequestHandler implements RequestHandlerInterface {

	private final Vector<RequestHandlerInterface> requestHandlers;
	private final ConcurrentHashMap<RequestType, Vector<Method>> requestHandlersList;
	
	public RequestHandler() {
		this.requestHandlers = new Vector<RequestHandlerInterface>();
		this.requestHandlersList = new ConcurrentHashMap<RequestType, Vector<Method>>();
		for (RequestType type : RequestType.values())
			requestHandlersList.put(type, new Vector<Method>());
	}
	
	public void registred(RequestHandlerInterface handler) throws Exception {
		for (Method method : handler.getClass().getMethods()) {
			if (method.isAnnotationPresent(RequestAnnotation.class)) {
				RequestAnnotation a = method.getAnnotation(RequestAnnotation.class);
				
			}
		}
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		
	}
	
}