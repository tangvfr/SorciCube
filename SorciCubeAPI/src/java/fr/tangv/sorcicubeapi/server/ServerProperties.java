package fr.tangv.sorcicubeapi.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bson.Document;

public class ServerProperties {
	
	public final int port;
	public final int backLog;
	public final InetAddress bindIP;
	public final long cooldownConnexion;
	public final long timeChecking;
	
	public ServerProperties(int port, int backLog, InetAddress bindIP, long cooldownConnexion, long timeChecking) {
		this.port = port;
		this.backLog = backLog;
		this.bindIP = bindIP;
		this.cooldownConnexion = cooldownConnexion;
		this.timeChecking = timeChecking;
	}
	
	public Document toDocument() {
		return new Document()
				.append("port", port)
				.append("backLog", backLog)
				.append("bindIP", bindIP.getHostName())
				.append("cooldownConnexion", cooldownConnexion)
				.append("timeChecking", timeChecking);
	}
	
	public static ServerProperties toServerProperties(Document doc) throws UnknownHostException {
		return new ServerProperties(
					doc.getInteger("port"),
					doc.getInteger("backLog"),
					InetAddress.getByName(doc.getString("bindIP")),
					doc.getInteger("cooldownConnexion"),
					doc.getInteger("timeChecking")
				);
	}
	
}
