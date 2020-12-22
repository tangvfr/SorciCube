package fr.tangv.sorcicubeapi.clients;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import fr.tangv.sorcicubeapi.requests.Request;
import fr.tangv.sorcicubeapi.requests.RequestException;
import fr.tangv.sorcicubeapi.requests.RequestHandlerInterface;
import fr.tangv.sorcicubeapi.requests.RequestType;

public class Client extends Thread {

	//init
	private volatile RequestHandlerInterface handler;
	private final Socket socket;
	private final long timeConnect;
	
	//stream
	private Scanner in;
	private OutputStreamWriter out;
	private volatile boolean connected;
	
	private volatile ClientIdentification clientID;
	
	public Client(Socket socket) {
		this.socket = socket;
		this.timeConnect = System.currentTimeMillis();
	}
	
	//seter geter
	
	public RequestHandlerInterface getHandler() {
		return handler;
	}

	public synchronized void setHandler(RequestHandlerInterface handler) {
		this.handler = handler;
	}

	public ClientIdentification getClientID() {
		return clientID;
	}

	public synchronized void setClientID(ClientIdentification clientID) {
		this.clientID = clientID;
	}
	
	//init
	
	public long getTimeConnect() {
		return timeConnect;
	}
	
	public long calcTimeConnected() {
		return System.currentTimeMillis()-timeConnect;
	}
	
	public boolean hasPastConnectTime(long cooldown) {
		return (System.currentTimeMillis()-timeConnect) > cooldown;
	}
	
	//stream
	
	public boolean isConnected() {
		return connected;
	}
	
	public synchronized void sendRequest(Request request) throws IOException {
		out.write(request.request+"\n");
		out.flush();
	}
	
	public void disconnect() throws IOException {
		socket.close();
	}
	
	@Override
	public void run() {
		try {
			this.in = new Scanner(socket.getInputStream(), StandardCharsets.US_ASCII.displayName());
			this.out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			while (socket.isConnected()) {
				try {
					Request request = new Request(in.nextLine());
					if (handler != null)
						handler.handlingRequest(this, request);
				} catch (RequestException e) {
					e.printStackTrace();
					try {
						sendRequest(new Request(RequestType.ERROR, "Invalid_Request", e.getMessage()));
					} catch (RequestException | IOException e2) {
						e2.printStackTrace();
					}
				} catch (Exception e) {
					if (socket.isConnected()) {
						e.printStackTrace();
						try {
							sendRequest(new Request(RequestType.ERROR, "Error Exception", e.getMessage()));
						} catch (RequestException | IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		}
		this.connected = false;
	}
	
}
