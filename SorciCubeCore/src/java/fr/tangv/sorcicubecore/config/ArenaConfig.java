package fr.tangv.sorcicubecore.config;

import org.bson.Document;

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
