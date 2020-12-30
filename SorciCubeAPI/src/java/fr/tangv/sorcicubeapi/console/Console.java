package fr.tangv.sorcicubeapi.console;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;

public class Console extends Thread {

	private SorciCubeAPI sorci;
	private Scanner in;
	
	public Console(SorciCubeAPI sorci) {
		this.sorci = sorci;
		this.in = new Scanner(System.in);
	}
	
	@Override
	public void run() {
		while (sorci.serverIsStart()) {
			try {
				String input = in.nextLine();
				int fs = input.indexOf((int) (' '));
				if (fs == -1)
					fs = input.length();
				String cmd = input.substring(0, fs);
				//String[] args = (input.length() >= fs+1) ? input.substring(fs+1).split(" ") : new String[0];
				String arg = (input.length() >= fs+1) ? input.substring(fs+1) : "";
				if (cmd.equalsIgnoreCase("stop")) {
					System.out.println("Server is Stopped !");
					sorci.stopServer();
				} else if (cmd.equalsIgnoreCase("help")) {
					System.out.println("Help:\r\n" + 
							" - help\r\n" + 
							" - stop\r\n" + 
							" - tokens\r\n" + 
							" - newtoken <description>\r\n" + 
							" - loadtokens\r\n" + 
							" - reloadconfig\r\n"+
							" - clients");
				} else if (cmd.equalsIgnoreCase("tokens")) {
					ConcurrentHashMap<String, String> tokens = sorci.getTokens();
					System.out.println("Tokens: "+tokens.size());
					for (String token : tokens.keySet()) {
						System.out.println("  Description: "+tokens.get(token));
						System.out.println("  "+token);
						System.out.println("  ------------");
					}
					System.out.println("-----END-----");
				} else if (cmd.equalsIgnoreCase("newtoken")) {
					if (!arg.isEmpty()) {
						String desc = arg;
						String token = sorci.generatedTokens(desc);
						sorci.saveTokens();
						System.out.println("NewToken: ");
						System.out.println("  Description: "+desc);
						System.out.println("  "+token);
						System.out.println("-----END-----");
					} else {
						System.out.println("newtoken <description>");
					}
				} else if (cmd.equalsIgnoreCase("loadtokens")) {
					sorci.loadTokens();
					System.out.println("Tokens is loaded !");
				} else if (cmd.equalsIgnoreCase("reloadconfig")) {
					System.out.println("This command is not disponible !");
				} else if (cmd.equalsIgnoreCase("clients")) {
					System.out.println("Clients: "+sorci.getClientsManager().getClients().size());
					for (Client client : sorci.getClientsManager().getClients()) {
						ClientIdentification cID = client.getClientID();
						String hex = Integer.toHexString(Byte.toUnsignedInt(cID.types));
						if (hex.length() == 2)
							hex = '0'+hex;
						System.out.println("  |"+hex+"| "+cID.name+" -> "+cID.token);
					}
					System.out.println("-----END-----");
				} else {
					System.out.println("Enter command \"help\" for helping !");
				}
			} catch (Exception e) {
				System.out.println("Error Console: "+e.getMessage());
			}
		}
		in.close();
	}
	
}
