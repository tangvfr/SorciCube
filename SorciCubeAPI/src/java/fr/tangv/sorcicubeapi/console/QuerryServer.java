package fr.tangv.sorcicubeapi.console;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import fr.tangv.sorcicubeapi.server.ServerProperties;

public class QuerryServer extends Thread {

	private final Console console;
	private final ServerProperties properties;
	private volatile long timeNextTry;
	
	public QuerryServer(Console console) {
		this.console = console;
		this.properties = console.sorci.getProperties();
		this.timeNextTry = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		if (!properties.querryEnable)
			return;
		try {
			ServerSocket server = new ServerSocket(properties.querryPort, properties.querryBackLog, properties.querryBindIP);
			while (!server.isClosed()) {
				try {
					Socket socket = server.accept();
					InputStream in = socket.getInputStream();
					int len;
					byte[] buf = new byte[16];
					while ((len = in.read(buf)) != -1)
						System.err.write(buf, 0, len);
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
	
}
