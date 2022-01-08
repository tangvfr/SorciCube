package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class CibleEnumConfig extends AbstractConfig {

	public StringConfig none;
	public StringConfig oneEntityAllyAndOneEntityEnemie;
	public StringConfig oneHero;
	public StringConfig allHero;
	public StringConfig one;
	public StringConfig oneEnemie;
	public StringConfig oneEntityEnemie;
	public StringConfig heroEnemie;
	public StringConfig allEnemie;
	public StringConfig allEntityEnemie;
	public StringConfig oneAlly;
	public StringConfig oneEntityAlly;
	public StringConfig heroAlly;
	public StringConfig allAlly;
	public StringConfig allEntityAlly;
	public StringConfig oneEntity;
	public StringConfig allEntity;
	public StringConfig all;

	public CibleEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}