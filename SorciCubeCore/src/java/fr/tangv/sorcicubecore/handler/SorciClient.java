package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import fr.tangv.sorcicubecore.clients.Client;

public class SorciClient extends Client {
	
	public SorciClient(URI uri) throws IOException, NumberFormatException, URISyntaxException {
		this(new SorciClientURI(uri));
	}
	
	public SorciClient(SorciClientURI uri) throws IOException {
		super(new Socket(uri.getAddr(), uri.getPort()));
		this.setClientID(uri.getClientID());
		
	}
	
	@Override
	public void disconnected() {
		
	}

}
