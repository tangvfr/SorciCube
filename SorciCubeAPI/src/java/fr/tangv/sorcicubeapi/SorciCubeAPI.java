package fr.tangv.sorcicubeapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import fr.tangv.sorcicubeapi.console.Console;
import fr.tangv.sorcicubeapi.handler.HandlerInit;
import fr.tangv.sorcicubeapi.server.ClientsManager;
import fr.tangv.sorcicubeapi.server.ServerAbstract;
import fr.tangv.sorcicubeapi.server.ServerProperties;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.Token;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerException;

public class SorciCubeAPI extends ServerAbstract {

	public static void main(String[] args) {
		try {
			new SorciCubeAPI();
		} catch (IOException | RequestException | RequestHandlerException e) {
			e.printStackTrace();
		}
	}
	
	private final Console console;
	private final HandlerInit handlerInit;
	private final ServerProperties properties;
	private final ClientsManager manager;
	private final ConcurrentHashMap<String, String> tokens;
	
	public SorciCubeAPI() throws UnknownHostException, IOException, RequestException, RequestHandlerException {
		this.properties = ServerProperties.toServerProperties(getDocumentProperties());
		this.manager = new ClientsManager(this);
		this.tokens = new ConcurrentHashMap<String, String>();
		loadTokens();
		this.handlerInit = new HandlerInit(this);
		this.console = new Console(this);
		this.start();
		System.out.println("Server is start on: port "+properties.port+", bindIP "+properties.bindIP+", backLog "+properties.backLog);
	}
	
	@Override
	public void started() {
		this.console.start();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void stoped() {
		this.console.stop();
	}
	
	public String generatedTokens(String desc) {
		String token = Token.generateToken();
		tokens.put(token, desc);
		return token;
	}
	
	public synchronized void loadTokens() throws IOException {
		File file = new File("tokens");
		if (!file.exists())
			saveTokens();
		tokens.clear();
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String desc = sc.nextLine();
			if (desc.startsWith("#") && sc.hasNextLine()) {
				tokens.put(sc.nextLine(), desc.substring(1));
			} else
				break;
		}
		sc.close();
	}
	
	public synchronized void saveTokens() throws IOException {
		File file = new File("tokens");
		if (!file.exists())
			file.createNewFile();
		PrintStream out = new PrintStream(file);
		for (String token : tokens.keySet()) {
			out.println("#"+tokens.get(token));
			out.println(token);
		}
		out.close();
	}
	
	private Document getDocumentProperties() throws IOException {
		File file = new File("./properties.json");
		if (!file.exists()) {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(("{\r\n" + 
					"	\"port\": 8367,\r\n" + 
					"	\"backLog\": 10,\r\n" + 
					"	\"bindIP\": \"0.0.0.0\",\r\n" + 
					"	\"cooldownConnexion\": 5000,\r\n"+ 
					"	\"timeChecking\": 1000\r\n" + 
					"}").getBytes(Client.CHARSET));
			out.close();
		}
		FileInputStream in = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		in.read(buf);
		in.close();
		return Document.parse(new String(buf, Client.CHARSET));
	}

	@Override
	public boolean hasToken(String token) {
		return tokens.containsKey(token);
	}

	@Override
	public ServerProperties getProperties() {
		return properties;
	}

	@Override
	public ClientsManager getClientsManager() {
		return manager;
	}

	public Console getConsole() {
		return console;
	}

	public HandlerInit getHandlerInit() {
		return handlerInit;
	}

	public ConcurrentHashMap<String, String> getTokens() {
		return tokens;
	}
	
}
