package fr.tangv.sorcicubecore.config;

import org.bson.Document;

public interface ElementConfig {

	//Construtor(Document doc);
	public Document toDocument() throws ConfigParseException;
	
}
