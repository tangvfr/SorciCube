package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class RarityEnumConfig extends AbstractConfig {

	public StringConfig COMMUN;
	public StringConfig RARE;
	public StringConfig EPIC;
	public StringConfig LEGENDARY;

	public RarityEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}