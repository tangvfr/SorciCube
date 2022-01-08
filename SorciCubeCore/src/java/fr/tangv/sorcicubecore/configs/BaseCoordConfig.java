package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.VectorConfig;

public class BaseCoordConfig extends AbstractConfig {

	public VectorConfig base;
	public VectorConfig entity1;
	public VectorConfig entity2;
	public VectorConfig entity3;
	public VectorConfig entity4;
	public VectorConfig entity5;
	
	public BaseCoordConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
