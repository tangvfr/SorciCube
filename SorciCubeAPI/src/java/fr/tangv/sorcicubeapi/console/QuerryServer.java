package fr.tangv.sorcicubeapi.console;

import java.net.ServerSocket;

import fr.tangv.sorcicubeapi.server.ServerProperties;

public class QuerryServer extends Thread {

	private final Console console;
	private final ServerProperties properties;
	private volatile long timeNextTry;
	
	public QuerryServer(Console console) {
		this.properties = console.sorci.getProperties();
		this.timeNextTry = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		if (!properties.querryEnable)
			return;
		ServerSocket server = new ServerSocket(properties.querryPort, properties.querryBackLog, properties.querryBindIP);
		server.
	}
	
	public boolean tryPassword(String password) {
		if (System.currentTimeMillis() > timeNextTry) {
			if (properties.querryPassword.equals(password))
				return true;
			else 
				this.timeNextTry = System.currentTimeMillis()+properties.querryTimeTryPassword;
		}
		return false;
	}
	
}
