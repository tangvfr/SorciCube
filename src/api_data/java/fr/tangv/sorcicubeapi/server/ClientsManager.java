package fr.tangv.sorcicubeapi.server;

import java.util.Vector;

import fr.tangv.sorcicubeapi.clients.Client;
import fr.tangv.sorcicubeapi.requests.RequestException;
import fr.tangv.sorcicubeapi.requests.RequestHandler;

public class ClientsManager extends Thread {

	public static final String VERSION_PROTOCOL = "0.1-beta";
	
	public final Vector<Client> clientsNotAuthentified;
	public final Vector<Client> clients;
	
	private final long timeConnexion;
	private final long timeChecking;
	private final Vector<String> tokens;
	
	private final RequestHandler handler;
	private final HandlerNotConnected handlerNotConnected;
	
	public ClientsManager(long timeConnexion, long timeChecking) throws RequestException {
		this.clients = new Vector<Client>();
		this.clientsNotAuthentified = new Vector<Client>();
		this.timeConnexion = timeConnexion;
		this.timeChecking = timeChecking;
		this.tokens = new Vector<String>();
		this.handlerNotConnected = new HandlerNotConnected(this);
		this.handler = new RequestHandler();
	}
	
	@Override
	public void run() {
		while (true) {
			
			
		}
	}
	
	public boolean authentification(Client client) {
		return tokens.contains(client.getClientID().token);
	}
	
}
