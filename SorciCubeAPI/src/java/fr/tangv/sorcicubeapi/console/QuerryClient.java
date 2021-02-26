package fr.tangv.sorcicubeapi.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class QuerryClient implements Runnable {

	private final QuerryCooldown cooldown;
	private final QuerryServer server;
	private final BufferedReader in;
	protected final Socket socket;
	protected final PrintStream out;
	protected volatile boolean auth;
	
	public QuerryClient(QuerryServer server, Socket socket) throws UnsupportedEncodingException, IOException {
		this.server = server;
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
		this.out = new PrintStream(socket.getOutputStream(), true, StandardCharsets.US_ASCII.displayName());
		this.auth = false;
		log("Connect-Start");
		out.println("[SorciCubeAPI]\r\nEnter your password:");
		this.cooldown = new QuerryCooldown(this);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			String input;
			while ((input = in.readLine()) != null) {
				if (auth) {
					Console.logger.info("Querry["+socket.getInetAddress().getHostAddress()+"] "+input);
					String back = server.console.excute(input);
					if (!back.isEmpty())
						out.print(back);
				} else {
					if (this.server.tryPassword(input)) {
						auth = true;
						out.println("You are authentified !");
						log("Connect-Authentified");
					} else {
						out.println("You are kicked because password invalid !");
						log("Connect-Wrong-Password");
						break;
					}
					cooldown.interrupt();
				}
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
