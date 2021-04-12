package fr.tangv.sorcicubecore.config;

import org.bson.Document;

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
