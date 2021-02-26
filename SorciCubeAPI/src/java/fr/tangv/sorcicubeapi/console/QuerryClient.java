package fr.tangv.sorcicubeapi.console;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import jline.console.ConsoleReader;

public class QuerryClient implements Runnable {

	private final QuerryCooldown cooldown;
	private final QuerryServer server;
	protected final Socket socket;
	protected ConsoleReader console;
	protected volatile boolean auth;
	
	public QuerryClient(QuerryServer server, Socket socket) throws UnsupportedEncodingException, IOException {
		this.server = server;
		this.socket = socket;
		this.console = new ConsoleReader(socket.getInputStream(), socket.getOutputStream());
		this.auth = false;
		log("Connect-Start");
		console.println("[SorciCubeAPI]\r\nEnter your password:");
		console.setPrompt(">");
		this.cooldown = new QuerryCooldown(this);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			if (this.server.tryPassword(console.readLine("*"))) {
				auth = true;
				console.println("You are authentified !");
				log("Connect-Authentified");
				cooldown.interrupt();
				String input;
				while ((input = console.readLine()) != null) {
					System.out.println("input: "+input);
					Console.logger.info("Querry["+socket.getInetAddress().getHostName()+"] "+input);
						
						
				}
			} else {
				console.println("You are kicked because password invalid !");
				log("Connect-Wrong-Password");
			}
			if (cooldown.isAlive())
				cooldown.interrupt();
			this.socket.close();
			log("Connect-Stop");
		} catch (IOException e) {
			Console.logger.warning(e.getMessage());
			try {
				this.socket.close();
			} catch (IOException e1) {}
			log("Error");
		}
	}
	
	protected void log(String message) {
		Console.logger.info("Querry["+socket.getInetAddress().getHostName()+"]-"+message);
	}
	
}
