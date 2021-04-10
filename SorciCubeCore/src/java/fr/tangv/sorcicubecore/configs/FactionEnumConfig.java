package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class FactionEnumConfig extends AbstractConfig {

	public StringConfig BASIC;
	public StringConfig DARK;
	public StringConfig LIGHT;
	public StringConfig NATURE;
	public StringConfig TOXIC;

	public FactionEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}