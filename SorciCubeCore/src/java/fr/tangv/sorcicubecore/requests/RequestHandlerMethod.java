package fr.tangv.sorcicubecore.requests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fr.tangv.sorcicubecore.clients.Client;

public class RequestHandlerMethod {

	private final RequestHandlerInterface handler;
	private final Method method;
	private final RequestAnnotation anot;
	
	public RequestHandlerMethod(RequestHandlerInterface handler, Method method, RequestAnnotation anot) throws RequestHandlerException {
		this.handler = handler;
		this.anot = anot;
		this.method = method;
		if (anot.type() != null) {
			Class<?>[] pt = method.getParameterTypes();
			if (pt.length == 2 && pt[0] == Client.class && pt[1] == Request.class)
				System.out.println("type retrun: "+method.getReturnType());
			else
				throw new RequestHandlerException("RequestAnnotation don't have RequestType");
		} else
			throw new RequestHandlerException("RequestAnnotation don't have RequestType");
	}
	
	public void execute(Client client, Request request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (anot.name() == null || anot.name().equals(request.name))
			method.invoke(handler, new Object[]{client, request});
	}
	
}
