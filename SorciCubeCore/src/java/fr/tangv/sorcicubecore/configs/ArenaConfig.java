package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ArenaConfig extends AbstractConfig {
 
	public StringConfig ID;
	public StringConfig NAME;
	public StringConfig WORLD;
	public IntegerConfig SPECTATOR_RADIUS;
	public BaseCoordConfig FIRST_BASE;
	public BaseCoordConfig SECOND_BASE;
	
	public ArenaConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
