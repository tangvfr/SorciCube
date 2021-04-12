package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class SortEnumConfig extends AbstractConfig {

	public StringConfig byId;
	public StringConfig byFaction;
	public StringConfig byRarity;
	public StringConfig byType;
	public StringConfig byLowMana;
	public StringConfig byHighMana;
	public StringConfig byName;

	public SortEnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}