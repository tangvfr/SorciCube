package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ArenaConfig extends AbstractConfig {

	public BooleanConfig isEnable;
	public StringConfig id;
	public StringConfig name;
	public StringConfig world;
	public IntegerConfig spectatorRadius;
	public BaseCoordConfig firstBase;
	public BaseCoordConfig secondBase;
	
	public ArenaConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
	@Override
	public String nameString() {
		return id.value;
	}
	
}
