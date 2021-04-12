package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class RarityEnumConfig extends AbstractConfig {

	public StringConfig commun;
	public StringConfig rare;
	public StringConfig epic;
	public StringConfig legendary;

	public RarityEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}