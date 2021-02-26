package fr.tangv.sorcicubeapi.console;

import java.io.IOException;

public class QuerryCooldown extends Thread {

	private final static long WAIT = 30_000L;
	private QuerryClient client;
	
	public QuerryCooldown(QuerryClient client) {
		this.client = client;
		this.start();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(WAIT);
			if (!client.auth)
				try {
					client.log("Connect-TimeOut");
					client.console.println("You are kicked because too time for connected !");
					client.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (InterruptedException e) {}
	}
	
}
