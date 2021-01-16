package fr.tangv.sorcicubecore.clients;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public abstract class Client extends Thread {

	public final static Charset CHARSET = StandardCharsets.UTF_16BE;
	public static final String VERSION_PROTOCOL = "0.1";
	
	//init
	private volatile RequestHandlerInterface handler;
	private final Socket socket;
	private final long timeConnect;
	
	//stream
	private final Scanner in;
	private final OutputStreamWriter out;
	
	private volatile ClientIdentification clientID;
	
	public Client(Socket socket) throws IOException {
		this.socket = socket;
		this.timeConnect = System.currentTimeMillis();
		this.in = new Scanner(socket.getInputStream(), StandardCharsets.US_ASCII.displayName());
		this.out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
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
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
	
	public boolean isConnected() {
		return socket.isConnected() && in.hasNextLine();
	}
	
	public synchronized void sendRequest(Request request) throws IOException {
		out.write(request.toRequest()+"\n");
		out.flush();
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
	public abstract void disconnect();
	
	@Override
	public void run() {
		while (isConnected()) {
			int id = -1;
			try {
				Request request = new Request(in.nextLine());
				id = request.id;
				if (handler != null)
					new Thread(() -> {
						try {
							handler.handlingRequest(this, request);
						} catch (Exception e) {
							e.printStackTrace();
							try {
								sendRequest(new Request(RequestType.ERROR, request.id, "Error_Exception", e.getMessage()));
							} catch (RequestException | IOException e2) {
								e2.printStackTrace();
							}
						}
					}).start();
			} catch (RequestException e) {
				e.printStackTrace();
				try {
					sendRequest(new Request(RequestType.ERROR, id, "Invalid_Request", e.getMessage()));
				} catch (RequestException | IOException e2) {
					e2.printStackTrace();
				}
			} catch (Exception e) {
				if (isConnected()) {
					e.printStackTrace();
					try {
						sendRequest(new Request(RequestType.ERROR, id, "Error_Exception", e.getMessage()));
					} catch (RequestException | IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		this.disconnect();
	}
	
}
