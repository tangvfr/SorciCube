package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ArenaConfig extends AbstractConfig {
 
	public StringConfig id;
	public StringConfig name;
	public StringConfig world;
	public IntegerConfig spectatorRadius;
	public BaseCoordConfig firstBase;
	public BaseCoordConfig secondBase;
	
	public ArenaConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
