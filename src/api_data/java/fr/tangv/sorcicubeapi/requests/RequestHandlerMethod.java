package fr.tangv.sorcicubeapi.requests;

import java.lang.reflect.Method;

import fr.tangv.sorcicubeapi.clients.Client;

public class RequestHandlerMethod {

	private final Method m;
	private final RequestAnnotation a;
	
	public RequestHandlerMethod(Method m, RequestAnnotation a) throws Exception {
		if (a.type() != null) {
			
			
		} else
			throw new RequestHandlerException("RequestAnnotation don't have RequestType");
	}
	
	public void execute(Client client, Request request) {
		
	}
	
	public RequestType getRequestType() {
		return a.type();
	}
	
}
