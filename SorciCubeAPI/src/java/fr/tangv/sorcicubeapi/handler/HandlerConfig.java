package fr.tangv.sorcicubeapi.handler;

import org.bson.Document;

public class HandlerConfig {

	private volatile Document doc;
	
	public HandlerConfig() {
		
		reload();
	}
	
	public void reload() {
		
	}
	
	public int getManyStartDecks() {
		return doc.getInteger("comany_start_decks");
	}
	
}
