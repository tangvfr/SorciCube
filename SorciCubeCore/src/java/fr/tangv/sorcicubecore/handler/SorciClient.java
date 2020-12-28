package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestHandler;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class SorciClient extends Client implements RequestHandlerInterface {
	
	public SorciClient(String uri) throws IOException, NumberFormatException, URISyntaxException, RequestHandlerException {
		this(new SorciClientURI(uri));
	}
	
	public SorciClient(URI uri) throws IOException, NumberFormatException, URISyntaxException, RequestHandlerException {
		this(new SorciClientURI(uri));
	}
	
	public SorciClient(SorciClientURI uri) throws IOException, RequestHandlerException {
		super(new Socket(uri.getAddr(), uri.getPort()));
		this.setClientID(uri.getClientID());
		RequestHandler handler = new RequestHandler();
		handler.registered(this);
		this.setHandler(handler);
	}
	
	@Override
	public void disconnected() {
		System.out.println("Is Stopped !!!!!!!!!!!!!!!!!!");
	}

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		System.out.println(request.id+": "+request.typeRequest+" n:"+request.name+"\ndata:"+request.data);
		
	}

	@RequestAnnotation(type=RequestType.AUTHENTIFIED)
	public void tamere(Client client, Request request) {
		System.out.println("You are authen");
	}
	
	public static void main(String[] args) {
		try {
			new SorciClient("sc://04:TestSorciClient:cacaou@localhost");
		} catch (NumberFormatException | IOException | URISyntaxException | RequestHandlerException e) {
			e.printStackTrace();
		}//:8367
	}
	
}
