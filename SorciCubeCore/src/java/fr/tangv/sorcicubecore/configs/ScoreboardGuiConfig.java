package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ScoreboardGuiConfig extends AbstractConfig {

	public StringConfig name;

	public ScoreboardGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}