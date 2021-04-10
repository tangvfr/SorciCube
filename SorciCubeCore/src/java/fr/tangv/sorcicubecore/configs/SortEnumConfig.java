package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class SortEnumConfig extends AbstractConfig {

	public StringConfig BY_ID;
	public StringConfig BY_FACTION;
	public StringConfig BY_RARITY;
	public StringConfig BY_TYPE;
	public StringConfig BY_LOW_MANA;
	public StringConfig BY_HIGH_MANA;
	public StringConfig BY_NAME;

	public SortEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}