package fr.tangv.sorcicubeapi.clients;

import org.bson.Document;

public class ClientIdentification {

	public final String version;
	public final byte types;
	public final String name;
	public final String token;
	
	public ClientIdentification(String version, byte types, String name, String token) {
		this.version = version;
		this.types = types;
		this.name = name;
		this.token = token;
	}
	
	public boolean isValid() {
		return version != null && types != 0 && name != null && token != null && !name.isEmpty();
	}
	
	public Document toDocument() {
		return new Document()
				.append("version", version)
				.append("types", types)
				.append("name", name)
				.append("token", token);
	}
	
	public static ClientIdentification toClientIdentification(Document document) {
		return new ClientIdentification(
					document.getString("version"),
					document.getInteger("types").byteValue(),
					document.getString("name"),
					document.getString("token")
				);
	}
	
}
