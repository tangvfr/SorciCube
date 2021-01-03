package fr.tangv.sorcicubeapi.handler;

import org.bson.Document;

public class HandlerServerConfig {

	private volatile Document doc;
	
	public HandlerServerConfig() {
		
		reload();
	}
	
	public void reload() {
		
	}
	
	public int getManyStartDecks() {
		return doc.getInteger("many_start_decks");
	}
	
}
