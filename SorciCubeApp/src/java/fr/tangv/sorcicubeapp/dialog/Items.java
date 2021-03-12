package fr.tangv.sorcicubeapp.dialog;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

public class Items extends ConcurrentHashMap<String, Item> {

	public Items() {
		super();
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = new InputStreamReader(ClassLoader.getSystemResourceAsStream("items.json"), StandardCharsets.UTF_8);
		char[] c = new char[1024];
		int len;
		while ((len = in.read(c)) != -1)
			sb.append(c, 0, len);
		in.close();
		//Document.parse(json)
	}
	
}
