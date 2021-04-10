package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class CibleEnumConfig extends AbstractConfig {

	public StringConfig NONE;
	public StringConfig ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE;
	public StringConfig ONE_HERO;
	public StringConfig ALL_HERO;
	public StringConfig ONE;
	public StringConfig ONE_ENEMIE;
	public StringConfig ONE_ENTITY_ENEMIE;
	public StringConfig HERO_ENEMIE;
	public StringConfig ALL_ENEMIE;
	public StringConfig ALL_ENTITY_ENEMIE;
	public StringConfig ONE_ALLY;
	public StringConfig ONE_ENTITY_ALLY;
	public StringConfig HERO_ALLY;
	public StringConfig ALL_ALLY;
	public StringConfig ALL_ENTITY_ALLY;
	public StringConfig ONE_ENTITY;
	public StringConfig ALL_ENTITY;
	public StringConfig ALL;

	public CibleEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}