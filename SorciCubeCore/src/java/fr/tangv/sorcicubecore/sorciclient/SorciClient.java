package fr.tangv.sorcicubecore.sorciclient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandler;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public abstract class SorciClient extends Client implements RequestHandlerInterface {
	
	private final HandlerResponse handlerReponse;
	private volatile boolean isAuthentified;
	private volatile PrintStream out;
	
	public SorciClient(String uri, long timeoutReponse) throws IOException, NumberFormatException, URISyntaxException, RequestHandlerException, RequestException {
		this(new SorciClientURI(uri), timeoutReponse);
	}
	
	public SorciClient(URI uri, long timeoutReponse) throws IOException, NumberFormatException, URISyntaxException, RequestHandlerException, RequestException {
		this(new SorciClientURI(uri), timeoutReponse);
	}
	
	public SorciClient(SorciClientURI uri, long timeoutReponse) throws IOException, RequestHandlerException, RequestException {
		super(new Socket(uri.getAddr(), uri.getPort()));
		this.setClientID(uri.getClientID());
		this.handlerReponse = new HandlerResponse(this, timeoutReponse);
		RequestHandler handler = new RequestHandler();
		handler.registered(this);
		handler.registered(handlerReponse);
		this.setHandler(handler);
		this.out = null;
		this.isAuthentified = false;
		sendRequest(new Request(RequestType.IDENTIFICATION, Request.randomID(), "identification", this.getClientID().toDocument().toJson()));
	}
	
	public Request sendRequestResponse(Request request, RequestType responseType) throws IOException, ResponseRequestException {
		Request reponse = this.handlerReponse.sendRequestReponse(request);
		if (reponse == null)
			throw new ResponseRequestException("Error with ReponseRequest is null !");
		if (reponse.requestType == RequestType.ERROR)
			throw new ResponseRequestException("Error ReponseRequest: "+reponse.data);
		if (reponse.requestType != responseType)
			throw new ResponseRequestException("Error type of ReponseRequest is invalid !");
		return reponse;
	}

	public void setPrintStream(PrintStream out) {
		this.out = out;
	}
	
	public PrintStream getPrintStream() {
		return this.out;
	}
	
	public synchronized boolean isAuthentified() {
		return isAuthentified;
	}

	@Override
	public void disconnect() {
		if (this.out != null) this.out.println("You are disconnected !");
		disconnected();
	}

	public abstract void connected();
	public abstract void disconnected();

	@RequestAnnotation(type=RequestType.KICK)
	public void kicked(Client client, Request request) {
		try {
			this.close();
		} catch (IOException e) {}
		if (this.out != null) this.out.println("You are kicked !");
	}
	
	@RequestAnnotation(type=RequestType.IDENTIFICATION_REFUSED)
	public void wrongAuth(Client client, Request request) {
		if (this.out != null) this.out.println("Wrong identification because "+request.data);
	}
	
	@RequestAnnotation(type=RequestType.COOLDOWN_AUTHENTIFIED)
	public void cooldownAuth(Client client, Request request) {
		if (this.out != null) this.out.println("You are pass cooldown for authentified !");
	}
	
	@RequestAnnotation(type=RequestType.ALREADY_AUTHENTIFIED)
	public void alreadyAuth(Client client, Request request) {
		if (this.out != null) this.out.println("You are already authentified !");
	}
	
	@RequestAnnotation(type=RequestType.DONT_AUTHENTIFIED)
	public void dontAuth(Client client, Request request) {
		if (this.out != null) this.out.println("You are don't authentified for this action !");
	}
	
	@RequestAnnotation(type=RequestType.AUTHENTIFIED)
	public void auth(Client client, Request request) {
		if (this.out != null) this.out.println("You are authentified !");
		this.isAuthentified = true;
		this.connected();
	}
	
}
