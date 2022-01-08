package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class IntegerConfig implements ElementConfig {

	public int value;

	public IntegerConfig(Document doc) {
		if (doc == null)
			this.value = 0;
		else
			this.value = doc.get("integer", 0);
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("integer", value);
	}

}
