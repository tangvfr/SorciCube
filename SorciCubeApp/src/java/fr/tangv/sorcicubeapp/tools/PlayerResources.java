package fr.tangv.sorcicubeapp.tools;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

public class PlayerResources {

	private final UUID uuid;
	private final String name;
	/*private final Image skin;
	private final Image head; */
	
	private PlayerResources(UUID uuid) throws ExceptionPlayerResources, IOException {
		this.uuid = uuid;
		if (uuid == null)
			throw new ExceptionPlayerResources("UUID is null");
		URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid.toString().replace("-", ""));
		InputStreamReader input = new InputStreamReader(url.openStream());
		CharArrayWriter w = new CharArrayWriter();
		char[] in = new char[512];
		int len;
		while ((len = input.read(in)) != -1)
			w.write(in, 0, len);
		input.close();
		String json = new String(w.toCharArray());
		if (json.isEmpty())
			throw new ExceptionPlayerResources("No found UUID");
		Document doc = Document.parse(json);
		String error = doc.getString("error");
		if (error != null)
			throw new ExceptionPlayerResources("Error "+error);
		this.name = doc.getString("name");
		
		List<Document> values = doc.getList("properties", Document.class);
		//name: textures
		System.out.println("names------");
		for (Document value : values) {
			System.out.println(value.getString("name"));
		}
		
	}
	
	public static void main(String[] args) {
		try {
			new PlayerResources(UUID.fromString("ffe5b376-832c-42e9-bf28-1f7d9363a73b"));
		} catch (ExceptionPlayerResources | IOException e) {
			e.printStackTrace();
		}
	}
	
}
