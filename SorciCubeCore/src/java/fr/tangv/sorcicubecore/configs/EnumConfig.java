package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class EnumConfig extends AbstractConfig {

	public CibleEnumConfig CIBLE;
	public FactionColorEnumConfig FACTION_COLOR;
	public FactionEnumConfig FACTION;
	public RarityEnumConfig RARITY;
	public TypeEnumConfig TYPE;
	public SortEnumConfig SORT;
	public FeatureEnumConfig FEATURE;

	public EnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}