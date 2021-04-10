package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class IntegerConfig implements ElementConfig {

	public int value;

	public IntegerConfig(Document doc) {
		this.value = doc.getInteger("integer");
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("integer", value);
	}

}
