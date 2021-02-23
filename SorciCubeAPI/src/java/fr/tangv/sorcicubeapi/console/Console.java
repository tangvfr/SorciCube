package fr.tangv.sorcicubeapi.console;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;

public class Console extends Thread {

	public static Logger logger;
	
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		logger = Logger.getLogger("logger");
		try {
			File file = new File("./logs");
			if (!file.exists())
				file.mkdirs();
			FileHandler fh = new FileHandler("./logs/"+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+"_%g.log", 16*1024*1024, 20, true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.exit(MAX_PRIORITY);
		}		
	}
	
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
					Console.logger.info("Server is Stopped !");
					sorci.stopServer();
				} else if (cmd.equalsIgnoreCase("help")) {
					Console.logger.info("Help:\r\n" + 
							" - help\r\n" + 
							" - stop\r\n" + 
							" - tokens\r\n" + 
							" - newtoken <description>\r\n" + 
							" - loadtokens\r\n" +
							" - clients");
				} else if (cmd.equalsIgnoreCase("tokens")) {
					ConcurrentHashMap<String, String> tokens = sorci.getTokens();
					int size = tokens.size();
					Console.logger.info("Tokens: "+size);
					if (size > 0)
						Console.logger.info("  ------------");
					for (String token : tokens.keySet()) {
						Console.logger.info("  Description: "+tokens.get(token));
						Console.logger.info("  "+token);
						Console.logger.info("  ------------");
					}
					Console.logger.info("-----END-----");
				} else if (cmd.equalsIgnoreCase("newtoken")) {
					if (!arg.isEmpty()) {
						String desc = arg;
						String token = sorci.generatedTokens(desc);
						sorci.saveTokens();
						Console.logger.info("NewToken: ");
						Console.logger.info("  Description: "+desc);
						Console.logger.info("  "+token);
						Console.logger.info("-----END-----");
					} else {
						Console.logger.info("newtoken <description>");
					}
				} else if (cmd.equalsIgnoreCase("loadtokens")) {
					sorci.loadTokens();
					Console.logger.info("Tokens is loaded !");
				} else if (cmd.equalsIgnoreCase("clients")) {
					Console.logger.info("Clients: "+sorci.getClientsManager().getClients().size());
					for (Client client : sorci.getClientsManager().getClients()) {
						ClientIdentification cID = client.getClientID();
						String hex = Integer.toHexString(Byte.toUnsignedInt(cID.types));
						if (hex.length() == 1)
							hex = '0'+hex;
						Console.logger.info("  "+formatTime(client.calcTimeConnected())+" | 0x"+hex+" | "+cID.name+" -> "+cID.token);
					}
					Console.logger.info("-----END-----");
				} else {
					Console.logger.info("Unknown command \""+cmd+"\" ! Enter command \"help\" for helping.");
				}
			} catch (Exception e) {
				Console.logger.warning("Error Console: "+e.getMessage());
			}
		}
		in.close();
	}
	
	private String complet(String src, int lenght, char c) {
		while (src.length() < lenght)
			src = c+src;
		return src;
	}
	
	private String complet(long src, int lenght) {
		return complet(Long.toString(src), lenght, '0');
	}
	
	private String formatTime(long time) {
		long h = time/3600_000;
		long m = (time%3600_000)/60_000;
		long s = ((time%3600_000)%60_000)/1000;
		return complet(h, 4)+":"+complet(m, 2)+":"+complet(s, 2);
	}
	
}