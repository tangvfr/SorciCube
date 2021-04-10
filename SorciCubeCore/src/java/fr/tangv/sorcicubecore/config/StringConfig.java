package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class StringConfig implements ElementConfig {

	public String value;

	public StringConfig(Document doc) {
		this.value = doc.getString("string");
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("string", value);
	}

}
