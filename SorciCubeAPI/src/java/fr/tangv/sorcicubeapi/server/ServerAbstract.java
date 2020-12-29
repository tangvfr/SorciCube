package fr.tangv.sorcicubeapi.server;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ServerAbstract extends Thread {
	
	private volatile ServerSocket server;
	
	@Override
	public void run() {
		ServerProperties properties = getProperties();
		ClientsManager manager = getClientsManager();
		try {
			try {
				this.server = new ServerSocket(properties.port, properties.backLog, properties.bindIP);
			} catch (IOException e1) {
				throw new IOException("ServerSokcet already started to this port !");
			}
			manager.start();
			while (!server.isClosed())
				manager.newClient(server.accept());
		} catch (IOException e) {
			if (!server.isClosed())
				e.printStackTrace();
		}
		this.server = null;
	}
	
	public synchronized boolean serverIsStart() {
		return server != null;
	}
	
	public synchronized void stopServer() throws IOException {
		if (server == null)
			throw new NullPointerException("Server is down");
		server.close();
	}
	
	public abstract boolean hasToken(String token);
	public abstract ServerProperties getProperties();
	public abstract ClientsManager getClientsManager();
	
}
