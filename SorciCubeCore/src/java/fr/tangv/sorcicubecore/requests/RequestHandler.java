package fr.tangv.sorcicubecore.requests;

import java.lang.reflect.Method;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.clients.Client;

public class RequestHandler implements RequestHandlerInterface {

	private final Vector<RequestHandlerInterface> requestHandlers;
	private final ConcurrentHashMap<RequestType, Vector<RequestHandlerMethod>> requestHandlersList;
	
	public RequestHandler() {
		this.requestHandlers = new Vector<RequestHandlerInterface>();
		this.requestHandlersList = new ConcurrentHashMap<RequestType, Vector<RequestHandlerMethod>>();
		for (RequestType type : RequestType.values())
			requestHandlersList.put(type, new Vector<RequestHandlerMethod>());
	}
	
	public void registered(RequestHandlerInterface handler) throws RequestHandlerException {
		if (handler.hashCode() == this.hashCode())
			throw new RequestHandlerException("Self registered don't possible !");
		for (Method method : handler.getClass().getMethods()) {
			RequestAnnotation anot = method.getAnnotation(RequestAnnotation.class);
			if (anot != null) {
				RequestHandlerMethod rhm = new RequestHandlerMethod(handler, method, anot);
				requestHandlersList.get(anot.type()).add(rhm);
			}
		}
		requestHandlers.add(handler);
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		for (RequestHandlerInterface handler : requestHandlers)
			handler.handlingRequest(client, request);
		for (RequestHandlerMethod method : requestHandlersList.get(request.typeRequest))
			method.execute(client, request);
	}
	
}