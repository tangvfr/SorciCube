package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class BooleanConfig implements ElementConfig {

	public boolean value;

	public BooleanConfig(Document doc) {
		this.value = doc.getBoolean("boolean");
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("boolean", value);
	}

}
