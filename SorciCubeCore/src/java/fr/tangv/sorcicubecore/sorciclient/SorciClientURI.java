package fr.tangv.sorcicubecore.sorciclient;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;

public class SorciClientURI {

	private final InetAddress addr;
	private final int port;
	private final ClientIdentification cID;
	
	public static String createURI(InetAddress addr, int port, byte types, String name, String token) {
		return "sc://"+Integer.toHexString(Byte.toUnsignedInt(types))+":"+name+":"+token+"@"+addr.getHostName()+((port > 0) ? ":"+port : "");
	}
	
	public SorciClientURI(String uri) throws NumberFormatException, UnknownHostException, URISyntaxException {
		this(new URI(uri));
	}
	
	public SorciClientURI(URI uri) throws URISyntaxException, UnknownHostException, NumberFormatException {
		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();
		if (port == -1)
			port = 8367;
		if (scheme == null || !scheme.equalsIgnoreCase("sc"))
			throw new URISyntaxException(uri.toString(), "Invalid scheme !");
		if (host == null || host.isEmpty())
			throw new URISyntaxException(uri.toString(), "Don't has host !");
		this.addr = InetAddress.getByName(host);
		this.port = port;
		//UserInfo
		String userInfo = uri.getUserInfo();
		if (userInfo == null)
			throw new URISyntaxException(uri.toString(), "Don't has UserInfo !");
		String[] id = userInfo.split(":");
		if (id.length != 3)
			throw new URISyntaxException(uri.toString(), "UserInfo is not complete !");
		byte types = Byte.parseByte(id[0], 16);
		this.cID = new ClientIdentification(Client.VERSION_PROTOCOL, types, id[1], id[2]);
		if (!cID.isValid())
			throw new URISyntaxException(uri.toString(), "ClientIdentification is not valid !");
	}
	
	public SorciClientURI(InetAddress addr, int port, byte types, String name, String token) throws URISyntaxException, UnknownHostException, NumberFormatException {
		this(addr, port, new ClientIdentification(Client.VERSION_PROTOCOL, types, name, token));
	}
	
	public SorciClientURI(InetAddress addr, int port, ClientIdentification cID) throws URISyntaxException, UnknownHostException, NumberFormatException {
		this.addr = addr;
		this.port = port;
		if (!cID.isValid())
			throw new URISyntaxException("", "ClientIdentification is not valid !");
		this.cID = cID;
	}

	public InetAddress getAddr() {
		return addr;
	}

	public int getPort() {
		return port;
	}

	public ClientIdentification getClientID() {
		return cID;
	}
	
	public String toURI() {
		return createURI(addr, port, cID.types, cID.name, cID.token);
	}
	
}
