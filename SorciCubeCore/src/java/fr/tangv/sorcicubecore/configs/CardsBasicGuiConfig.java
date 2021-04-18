package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class CardsBasicGuiConfig extends BasicGuiConfig {

	public StringConfig sort;
	public StringConfig previous;
	public StringConfig next;
	public StringConfig page;
	
	public CardsBasicGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
