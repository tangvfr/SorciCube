package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class FactionColorEnumConfig extends AbstractConfig {

	public StringConfig basic;
	public StringConfig dark;
	public StringConfig light;
	public StringConfig nature;
	public StringConfig toxic;

	public FactionColorEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}