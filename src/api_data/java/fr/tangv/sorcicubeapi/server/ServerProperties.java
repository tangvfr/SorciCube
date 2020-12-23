package fr.tangv.sorcicubeapi.server;

import java.net.InetAddress;

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
	
}
