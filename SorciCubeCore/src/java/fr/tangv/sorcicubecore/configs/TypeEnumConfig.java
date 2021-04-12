package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class TypeEnumConfig extends AbstractConfig {

	public StringConfig spell;
	public StringConfig entity;
	public StringConfig hero;

	public TypeEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}