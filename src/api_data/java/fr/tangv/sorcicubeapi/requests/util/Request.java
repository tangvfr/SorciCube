package fr.tangv.sorcicubeapi.requests.util;

import java.util.Base64;

import fr.tangv.sorcicubeapi.SorcicubeApi;

public class Request {

	public static final String version = "0.1-beta";
	
	public final TypeRequest typeRequest;
	public final String name;
	public final TypeData typeData;
	public final String data;
	
	public Request(String request) throws Exception {
		String[] r = request.split(" ");
		if (r.length != 4)
			throw new Exception("Format space in request is invalid");
		try {
			this.typeRequest = TypeRequest.valueOf(r[0]);
			this.name = r[1];
			if (name.contains(" ") || name.contains("\n") || name.contains("\r"))
				throw new Exception("");
			this.typeData = TypeData.valueOf(r[2]);
			this.data = new String(Base64.getDecoder().decode(r[3]), SorcicubeApi.CHARSET);
		} catch (Exception e) {
			throw new Exception("Format of request is invalid");
		}
	}
	
	public Request(TypeRequest typeRequest, String name, TypeData typeData, String data) throws Exception {
		this.typeRequest = typeRequest;
		this.name = name;
		if (name.contains(" ") || name.contains("\n") || name.contains("\r"))
			throw new Exception("Name of request is invalid");
		this.typeData = typeData;
		this.data = data;
	}
	
	public String toRequest() {
		return typeRequest.name()+" "+name+" "+typeData.toString()+" "+Base64.getEncoder().encode(data.getBytes(SorcicubeApi.CHARSET));
	}
	
}
