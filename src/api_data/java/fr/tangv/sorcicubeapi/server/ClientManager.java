package fr.tangv.sorcicubeapi.server;

import java.util.Vector;

import fr.tangv.sorcicubeapi.clients.Client;
import fr.tangv.sorcicubeapi.requests.Request;

public class ClientManager implements Runnable {

	public static final String VERSION_PROTOCOL = "0.1-beta";
	
	private Vector<String> tokens;
	
	private Vector<Client> clientsConnexion;
	private Vector<Client> clientsSpigot;
	private Vector<Client> clientsOther;
	private Vector<Client> clients;
	
	public ClientManager() {
	
	}
	
	public void newConnexion() {
		
	}
	
	@Override
	public void run() {
		
	}
	
	public void disconnect(Request request) {
		for (Client client : clients)
			;
	}
	
	public boolean authentification(Client client) {
		
		
		
		return false;
	}
	
}
