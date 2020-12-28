package fr.tangv.sorcicubecore.requests;

import java.util.Base64;

import fr.tangv.sorcicubecore.clients.Client;

public class Request {
	
	public final RequestType typeRequest;
	public final int id;
	public final String name;
	public final String data;
	public final String request;
	
	public static int randomID() {
		return (int) (Math.random()*Integer.MAX_VALUE);
	}
	
	public Request(String request) throws RequestException {
		String[] r = request.split(" ");
		if (r.length != 4)
			throw new RequestException("Format space in request is invalid");
		try {
			this.typeRequest = RequestType.valueOf(r[0]);
			this.id = Integer.parseInt(r[1]);
			this.name = r[2];
			if (name.contains(" ") || name.contains("\n") || name.contains("\r"))
				throw new Exception("");
			this.data = new String(Base64.getDecoder().decode(r[3]), Client.CHARSET);
			this.request = request;
		} catch (Exception e) {
			throw new RequestException(request+"\nFormat of request is invalid");
		}
	}
	
	public Request(RequestType typeRequest, int id, String name, String data) throws RequestException {
		this.typeRequest = typeRequest;
		this.id = id;
		this.name = name;
		if (name.contains(" ") || name.contains("\n") || name.contains("\r"))
			throw new RequestException("Name of request is invalid");
		if (data == null)
			throw new RequestException("Data is Null");
		this.data = data;
		this.request = typeRequest.name()+" "+name+" "+Base64.getEncoder().encode(data.getBytes(Client.CHARSET));
	}
	
}
