package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ActionMessageConfig extends AbstractConfig {

	public StringConfig withAction;
	public StringConfig withoutAction;
	
	public ActionMessageConfig(Document doc) throws ConfigParseException {
		super(doc);
	}

}
