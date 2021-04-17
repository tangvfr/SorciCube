package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class BasicGuiConfig extends AbstractConfig {
	
	public StringConfig name;
	
	public BasicGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}