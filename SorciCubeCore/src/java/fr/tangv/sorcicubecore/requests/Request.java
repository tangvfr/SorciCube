package fr.tangv.sorcicubecore.requests;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import fr.tangv.sorcicubecore.clients.Client;

public class Request {
	
	public final RequestType requestType;
	public final int id;
	public final String name;
	public final String data;
	
	public static int randomID() {
		return (int) (Math.random()*Integer.MAX_VALUE);
	}
	
	public Request(String request) throws RequestException {
		String[] r = request.split(" ");
		if (r.length != 4 && r.length != 3)
			throw new RequestException("Format space in request is invalid");
		try {
			this.requestType = RequestType.valueOf(r[0]);
			this.id = Integer.parseInt(r[1]);
			this.name = r[2];
			if (name.contains(" ") || name.contains("\n") || name.contains("\r") || name.isEmpty())
				throw new Exception("");
			this.data = (r.length == 3) ? "" : new String(Base64.getDecoder().decode(r[3]), Client.CHARSET);
		} catch (Exception e) {
			throw new RequestException("Format of request is invalid: "+request);
		}
	}
	
	public Request(RequestType requestType, int id, String name, String data) throws RequestException {
		this.requestType = requestType;
		this.id = id;
		this.name = name;
		if (name.contains(" ") || name.contains("\n") || name.contains("\r"))
			throw new RequestException("Name of request is invalid");
		if (requestType.getTypeData() != RequestDataType.NOTHING && (data == null))
			throw new RequestException("Data is Null");
		this.data = data;
	}
	
	public Request createReponse(RequestType typeRequest, String data) throws RequestException {
		return new Request(typeRequest, id, name, data);
	}
	
	public Request createReponse(RequestType typeRequest, String name, String data) throws RequestException {
		return new Request(typeRequest, id, name, data);
	}
	
	public String toRequest() {
		return requestType.name()+" "+id+" "+name+" "+new String(Base64.getEncoder().encode((requestType.getTypeData() != RequestDataType.NOTHING ? data : RequestDataType.NOTHING.name()).getBytes(Client.CHARSET)), StandardCharsets.US_ASCII);
	}
	
	public String toRequestNoData() {
		return requestType.name()+" "+id+" "+name+" Data["+(requestType.getTypeData() != RequestDataType.NOTHING ? data : RequestDataType.NOTHING.name()).length()+"]";
	}
	
}
