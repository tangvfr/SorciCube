package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class BooleanConfig implements ElementConfig {

	public boolean value;

	public BooleanConfig(Document doc) {
		if (doc == null)
			this.value = false;
		else
			this.value = doc.get("boolean", false);
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("boolean", value);
	}

}
