package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public class LongConfig implements ElementConfig {

	public long value;

	public LongConfig(Document doc) {
		this.value = doc.getLong("long");
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		return new Document("long", value);
	}

}