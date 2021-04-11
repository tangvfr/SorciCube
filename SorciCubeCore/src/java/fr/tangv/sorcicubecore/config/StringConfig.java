package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class StringConfig implements ElementConfig {

	public String value;

	public StringConfig(Document doc) {
		if (doc == null)
			this.value = "";
		else
			this.value = doc.get("string", "");
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("string", value);
	}

}
