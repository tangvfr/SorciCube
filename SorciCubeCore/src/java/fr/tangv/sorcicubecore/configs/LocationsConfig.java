package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.LocationConfig;

public class LocationsConfig extends AbstractConfig {

	public LocationConfig locationSpawn;
	public LocationConfig locationTuto;
	public LocationConfig locationNoclassified;
	public LocationConfig locationDuel;
	public LocationConfig locationNpc;
	
	public LocationsConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
