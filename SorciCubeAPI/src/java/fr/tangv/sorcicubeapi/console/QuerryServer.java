package fr.tangv.sorcicubeapi.console;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.tangv.sorcicubeapi.server.ServerProperties;

public class QuerryServer extends Thread {

	private final Console console;
	private final ServerProperties properties;
	private volatile long timeNextTry;
	private final boolean enable;
	
	public QuerryServer(Console console) {
		this.console = console;
		this.properties = console.sorci.getProperties();
		this.timeNextTry = System.currentTimeMillis();
		this.enable = properties.querryEnable;
	}
	
	//for test
	private QuerryServer() throws UnknownHostException {
		this.console = null;
		this.properties = ServerProperties.createDefault();
		this.timeNextTry = System.currentTimeMillis();
		this.enable = true;
	}
	
	@Override
	public void run() {
		if (!enable)
			return;
		try {
			ServerSocket server = new ServerSocket(properties.querryPort, properties.querryBackLog, properties.querryBindIP);
			while (!server.isClosed()) {
				try {
					Socket socket = server.accept();
					InputStream in = socket.getInputStream();
					
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
	
	public static void main(String[] args) throws UnknownHostException {
		new QuerryServer().start();
	}
	
}
