package fr.tangv.sorcicubeapi.client;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

public class Client extends Thread {

	//init
	private final ClientManager manager;
	private final Socket socket;
	private final long timeConnect;
	private boolean identified;
	
	//stream
	private InputStreamReader in;
	private OutputStreamWriter out;
	private boolean connected;
	
	//client
	private String version;
	private int types;
	private String id;
	private String token;
	
	public Client(ClientManager manager, Socket socket) {
		this.manager = manager;
		this.socket = socket;
		this.identified = false;
		this.timeConnect = System.currentTimeMillis();
		this.connected = false;
		start();
	}
	
	//init
	
	public boolean isIdentified() {
		return identified;
	}
	
	public long getTimeConnect() {
		return timeConnect;
	}
	
	public boolean hasPastConnectTime(long cooldown) {
		return (System.currentTimeMillis()-timeConnect) > cooldown;
	}
	
	//stream
	
	public boolean isConnected() {
		return connected;
	}
	
	//client
	
	public String getVersion() {
		return version;
	}
	
	public int getTypes() {
		return types;
	}
	
	public String getID() {
		return id;
	}
	
	public String getToken() {
		return token;
	}
	
	@Override
	public void run() {
		this.connected = true;
		try {
			this.in = new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII);
			this.out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
			
			
			while (socket.isConnected()) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.connected = false;
	}
	
}
