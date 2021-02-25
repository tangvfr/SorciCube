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
	public final boolean querryEnable;
	public final String querryPassword;
	public final int querryPort;
	public final int querryBackLog;
	public final InetAddress querryBindIP;
	public final long querryTimeTryPassword;
	
	public ServerProperties(int port,
			int backLog,
			InetAddress bindIP,
			long cooldownConnexion,
			long timeChecking,
			boolean querryEnable,
			String querryPassword,
			int querryPort,
			int querryBackLog,
			InetAddress querryBindIP,
			long querryTimeTryPassword) {
		this.port = port;
		this.backLog = backLog;
		this.bindIP = bindIP;
		this.cooldownConnexion = cooldownConnexion;
		this.timeChecking = timeChecking;
		this.querryEnable = querryEnable;
		this.querryPassword = querryPassword;
		this.querryPort = querryPort;
		this.querryBackLog = querryBackLog;
		this.querryBindIP = querryBindIP;
		this.querryTimeTryPassword = querryTimeTryPassword;
	}
	
	public Document toDocument() {
		return new Document()
				.append("port", port)
				.append("backLog", backLog)
				.append("bindIP", bindIP.getHostName())
				.append("cooldownConnexion", cooldownConnexion)
				.append("timeChecking", timeChecking)
				.append("querryEnable", querryEnable)
				.append("querryPassword", querryPassword)
				.append("querryPort", querryPort)
				.append("querryBackLog", querryBackLog)
				.append("querryBindIP", querryBindIP.getHostName())
				.append("querryTimeTryPassword", querryTimeTryPassword);
	}
	
	public static ServerProperties toServerProperties(Document doc) throws UnknownHostException {
		return new ServerProperties(
					doc.getInteger("port", 8367),
					doc.getInteger("backLog", 10),
					InetAddress.getByName(doc.get("bindIP", "0.0.0.0")),
					doc.getInteger("cooldownConnexion", 5000),
					doc.getInteger("timeChecking", 1000),
					doc.getBoolean("querryEnable", false),
					doc.get("querryPassword", "YourPassword"),
					doc.getInteger("querryPort", 8368),
					doc.getInteger("querryBackLog", 10),
					InetAddress.getByName(doc.get("querryBindIP", "0.0.0.0")),
					doc.getInteger("querryTimeTryPassword", 3000)
				);
	}
	
	public static ServerProperties createDefault() throws UnknownHostException {
		return toServerProperties(new Document());
	}
	
}
