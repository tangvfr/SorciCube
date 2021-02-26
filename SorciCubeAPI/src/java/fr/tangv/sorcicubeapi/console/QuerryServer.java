package fr.tangv.sorcicubeapi.console;

import java.io.IOException;
import java.net.ServerSocket;

import fr.tangv.sorcicubeapi.server.ServerProperties;

public class QuerryServer extends Thread {

	protected final Console console;
	private final ServerProperties properties;
	private volatile long timeNextTry;
	private final boolean enable;
	
	public QuerryServer(Console console) {
		this.console = console;
		this.properties = console.sorci.getProperties();
		this.timeNextTry = System.currentTimeMillis();
		this.enable = properties.querryEnable;
	}
	
	@Override
	public void run() {
		if (!enable)
			return;
		try {
			ServerSocket server = new ServerSocket(properties.querryPort, properties.querryBackLog, properties.querryBindIP);
			while (true) {
				try {
					new QuerryClient(this, server.accept());
				} catch (Exception e) {
					Console.logger.warning(e.getMessage());
					e.printStackTrace();
					break;
				}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
