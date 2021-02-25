package fr.tangv.sorcicubeapi.console;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	protected SorciCubeAPI sorci;
	private java.io.Console console;
	private QuerryServer querry;
	
	public Console(SorciCubeAPI sorci) throws IOException {
		this.sorci = sorci;
		this.console = System.console();
		this.querry = new QuerryServer(this);
	}
	
	@Override
	public void run() {
		querry.start();
		while (sorci.serverIsStart()) {
			try {
				String input = this.console.readLine();
				if (input == null) {
					sorci.stopServer();
					return;
				}
				Console.logger.info(this.excute(input));
			} catch (Exception e) {
				Console.logger.warning("Error Console: "+e.getMessage());
			}
		}
	}
	
	public String excute(String input) throws IOException {
		if (input == null || input.isEmpty())
			return "";
		StringBuilder out = new StringBuilder();
		int fs = input.indexOf((int) (' '));
		if (fs == -1)
			fs = input.length();
		String cmd = input.substring(0, fs);
		//String[] args = (input.length() >= fs+1) ? input.substring(fs+1).split(" ") : new String[0];
		String arg = (input.length() >= fs+1) ? input.substring(fs+1) : "";
		if (cmd.equalsIgnoreCase("stop")) {
			out.append("Server is Stopped !\r\n");
			sorci.stopServer();
		} else if (cmd.equalsIgnoreCase("help")) {
			out.append("Help:\r\n" + 
					" - help\r\n" + 
					" - stop\r\n" + 
					" - tokens\r\n" + 
					" - newtoken <description>\r\n" + 
					" - loadtokens\r\n" +
					" - clients\r\n");
		} else if (cmd.equalsIgnoreCase("tokens")) {
			ConcurrentHashMap<String, String> tokens = sorci.getTokens();
			int size = tokens.size();
			out.append("Tokens: "+size+"\r\n");
			if (size > 0)
				out.append("  ------------"+"\r\n");
			for (String token : tokens.keySet()) {
				out.append("  Description: "+tokens.get(token)+"\r\n");
				out.append("  "+token+"\r\n");
				out.append("  ------------"+"\r\n");
			}
			out.append("-----END-----"+"\r\n");
		} else if (cmd.equalsIgnoreCase("newtoken")) {
			if (!arg.isEmpty()) {
				String desc = arg;
				String token = sorci.generatedTokens(desc);
				sorci.saveTokens();
				out.append("NewToken: "+"\r\n");
				out.append("  Description: "+desc+"\r\n");
				out.append("  "+token+"\r\n");
				out.append("-----END-----"+"\r\n");
			} else {
				out.append("newtoken <description>"+"\r\n");
			}
		} else if (cmd.equalsIgnoreCase("loadtokens")) {
			sorci.loadTokens();
			out.append("Tokens is loaded !"+"\r\n");
		} else if (cmd.equalsIgnoreCase("clients")) {
			out.append("Clients: "+sorci.getClientsManager().getClients().size()+"\r\n");
			for (Client client : sorci.getClientsManager().getClients()) {
				ClientIdentification cID = client.getClientID();
				String hex = Integer.toHexString(Byte.toUnsignedInt(cID.types));
				if (hex.length() == 1)
					hex = '0'+hex;
				out.append("  "+formatTime(client.calcTimeConnected())+" | 0x"+hex+" | "+cID.name+" -> "+cID.token+"\r\n");
			}
			out.append("-----END-----"+"\r\n");
		} else {
			out.append("Unknown command \""+cmd+"\" ! Enter command \"help\" for helping."+"\r\n");
		}
		return out.toString();
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