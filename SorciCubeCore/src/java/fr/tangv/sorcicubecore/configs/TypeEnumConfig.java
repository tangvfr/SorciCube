package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class TypeEnumConfig extends AbstractConfig {

	public StringConfig SPELL;
	public StringConfig ENTITY;
	public StringConfig HERO;

	public TypeEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}