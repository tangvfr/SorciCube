package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.VectorConfig;

public class BaseCoordConfig extends AbstractConfig {

	public VectorConfig BASE;
	public VectorConfig ENTITY1;
	public VectorConfig ENTITY2;
	public VectorConfig ENTITY3;
	public VectorConfig ENTITY4;
	public VectorConfig ENTITY5;
	
	public BaseCoordConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
