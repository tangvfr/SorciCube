package fr.tangv.sorcicubeapp.dialog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

public class Items extends ConcurrentHashMap<String, Item> {

	private static final long serialVersionUID = 5016116064499022814L;

	public Items() throws IOException {
		super();
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = new InputStreamReader(ClassLoader.getSystemResourceAsStream("items.json"), StandardCharsets.UTF_8);
		char[] c = new char[1024];
		int len;
		while ((len = in.read(c)) != -1)
			sb.append(c, 0, len);
		in.close();
		Document items = Document.parse(sb.toString());
		for (String key : items.keySet())
			this.put(key, Item.toItem(items.get(key, Document.class)));
	}
	
}
