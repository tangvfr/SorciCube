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
	
	private final HandlerReponse handlerReponse;
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
		this.handlerReponse = new HandlerReponse(this, timeoutReponse);
		RequestHandler handler = new RequestHandler();
		handler.registered(this);
		handler.registered(handlerReponse);
		this.setHandler(handler);
		sendRequest(new Request(RequestType.IDENTIFICATION, Request.randomID(), "identification", this.getClientID().toDocument().toJson()));
		this.out = System.out;
	}
	
	public Request sendRequestReponse(Request request) throws IOException {
		return this.handlerReponse.sendRequestReponse(request);
	}
	
	public void setPrintStream(PrintStream out) {
		this.out = out;
	}
	
	public PrintStream getPrintStream() {
		return this.out;
	}
	
	@Override
	public void disconnect() {
		this.out.println("You are disconnected !");
		disconnected();
	}

	public abstract void connected();
	public abstract void disconnected();

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		//this.out.println(request.id+": "+request.typeRequest+" n:"+request.name+"\ndata: "+request.data);
	}

	@RequestAnnotation(type=RequestType.KICK)
	public void kicked(Client client, Request request) {
		try {
			this.close();
		} catch (IOException e) {}
		this.out.println("You are kicked !");
	}
	
	@RequestAnnotation(type=RequestType.IDENTIFICATION_REFUSED)
	public void wrongAuth(Client client, Request request) {
		this.out.println("Wrong identification because "+request.data);
	}
	
	@RequestAnnotation(type=RequestType.COOLDOWN_AUTHENTIFIED)
	public void cooldownAuth(Client client, Request request) {
		this.out.println("You are pass cooldown for authentified !");
	}
	
	@RequestAnnotation(type=RequestType.ALREADY_AUTHENTIFIED)
	public void alreadyAuth(Client client, Request request) {
		this.out.println("You are already authentified !");
	}
	
	@RequestAnnotation(type=RequestType.DONT_AUTHENTIFIED)
	public void dontAuth(Client client, Request request) {
		this.out.println("You are don't authentified !");
	}
	
	@RequestAnnotation(type=RequestType.AUTHENTIFIED)
	public void auth(Client client, Request request) {
		this.out.println("You are authentified !");
		this.connected();
	}
	
	public static void main(String[] args) {
		try {
			String token = "8tW3cFg4xgrGoybGzcPcwKMhadJmBEOhCMexlctqD4yCJxk6j1oS6MDngpTyQJKn";
			SorciClient sc = new SorciClient("sc://04:TestSorciClient:"+token+"@localhost:8367", 10_000) {
				@Override public void connected() {}
				@Override public void disconnected() {}
			};
			sc.start();
		} catch (NumberFormatException | IOException | URISyntaxException | RequestHandlerException | RequestException e) {
			e.printStackTrace();
		}//:8367
	}
	
}
