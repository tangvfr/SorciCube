package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class EnumConfig extends AbstractConfig {

	public CibleEnumConfig cible;
	public FactionColorEnumConfig factionColor;
	public FactionEnumConfig faction;
	public RarityEnumConfig rarity;
	public TypeEnumConfig type;
	public SortEnumConfig sort;
	public FeatureEnumConfig feature;

	public EnumConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}