package fr.tangv.sorcicubeapi.server;

import java.io.IOException;
import java.net.ServerSocket;

import fr.tangv.sorcicubeapi.console.Console;

public abstract class ServerAbstract extends Thread {
	
	private volatile ServerSocket server;
	
	@Override
	public void run() {
		ServerProperties properties = getProperties();
		ClientsManager manager = getClientsManager();
		try {
			try {
				this.server = new ServerSocket(properties.port, properties.backLog, properties.bindIP);
			} catch (Exception e1) {
				Console.logger.warning("ServerSokcet already started to this port !");
			}
			manager.start();
			started();
			while (!server.isClosed())
				manager.newClient(server.accept());
		} catch (Exception e) {
			if (serverIsStart() && !server.isClosed())
				e.printStackTrace();
		}
		if (server != null && !server.isClosed())
			try {
				server.close();
			} catch (IOException e) {}
		this.server = null;
		stoped();
	}
	
	public synchronized boolean serverIsStart() {
		return server != null;
	}
	
	public synchronized void stopServer() throws IOException {
		if (server == null)
			throw new NullPointerException("Server already down !");
		server.close();
	}
	
	public abstract void started();
	public abstract void stoped();
	public abstract boolean hasToken(String token);
	public abstract ServerProperties getProperties();
	public abstract ClientsManager getClientsManager();
	
}
