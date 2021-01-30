package fr.tangv.sorcicubeapi.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandler;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class ClientsManager extends Thread {
	
	private final Request requestCooldown;
	
	public final Vector<Client> clientsNotAuthentified;
	public final Vector<Client> clients;
	
	private final ServerAbstract server;
	private final long cooldownConnexion;
	private final long timeChecking;
	
	private final RequestHandler handler;
	private final HandlerNotConnected handlerNotConnected;
	
	public ClientsManager(ServerAbstract server) throws RequestException, RequestHandlerException {
		this.server = server;
		this.cooldownConnexion = server.getProperties().cooldownConnexion;
		this.timeChecking = server.getProperties().timeChecking;
		this.clients = new Vector<Client>();
		this.clientsNotAuthentified = new Vector<Client>();
		this.handlerNotConnected = new HandlerNotConnected(this);
		this.handler = new RequestHandler();
		this.handler.registered(new RequestHandlerInterface() {
			
			@Override
			public void handlingRequest(Client client, Request request) throws Exception {
				if (request.requestType == RequestType.IDENTIFICATION)
					client.sendRequest(new Request(RequestType.ALREADY_AUTHENTIFIED, request.id, "AlreadyAuthentified", ""));
			}
			
		});
		this.requestCooldown = new Request(RequestType.COOLDOWN_AUTHENTIFIED, -2, "COOLDOWN_AUTHENTIFIED", "");
	}
	
	public void registered(RequestHandlerInterface handler) throws RequestHandlerException {
		this.handler.registered(handler);
	}
	
	private class ClientServer extends Client {

		public ClientServer(Socket socket) throws IOException {
			super(socket);
		}

		@Override
		public void disconnect() {
			if (clientsNotAuthentified.contains(this)) {
				clientsNotAuthentified.remove(this);
				HandlerNotConnected.printDisconnect(this, false);
			} else if (clients.contains(this)) {
				clients.remove(this);
				HandlerNotConnected.printDisconnect(this, true);
			}
		}
		
	}
	
	public void newClient(Socket socket) throws IOException {
		ClientServer client = new ClientServer(socket);
		client.setHandler(handlerNotConnected);
		clientsNotAuthentified.add(client);
		client.start();
	}
	
	@Override
	public void run() {
		while (server.isAlive()) {
			//ClientsNotAuthentified
			Iterator<Client> itnc = clientsNotAuthentified.iterator();
			while (itnc.hasNext()) {
				Client client = itnc.next();
				if (client.hasPastConnectTime(cooldownConnexion))
					try {
						client.sendRequest(requestCooldown);
						client.close();
					} catch (IOException e1) {}
			}
			try {
				Thread.sleep(timeChecking);
			} catch (InterruptedException e) {}
		}
		closeAllClient();
	}
	
	private void closeAllClient() {
		for (Client client : clientsNotAuthentified)
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		for (Client client : clients)
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public boolean authentification(Client client) {
		if (server.hasToken(client.getClientID().token)) {
			clientsNotAuthentified.remove(client);
			clients.add(client);
			client.setHandler(handler);
			return true;
		} else
			return false;
	}

	public Vector<Client> getClients() {
		return clients;
	}
	
}