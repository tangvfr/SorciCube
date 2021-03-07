package fr.tangv.sorcicubeapp.dialog;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

public class Items extends ConcurrentHashMap<String, Item> {

	public Items() {
		super();
		InputStreamReader in = new InputStreamReader(ClassLoader.getSystemResourceAsStream("/items/index"), StandardCharsets.UTF_8);
		in.read(arg0)
		Document.parse(json)
	}
	
}
