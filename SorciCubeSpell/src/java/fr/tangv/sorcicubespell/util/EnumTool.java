package fr.tangv.sorcicubespell.util;

import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue;

public class EnumTool {

	private ConfigurationSection cible;
	private ConfigurationSection faction;
	private ConfigurationSection rarity;
	private ConfigurationSection type;
	private ConfigurationSection sort;
	private ConfigurationSection feature;
	
	public EnumTool(Config config) {
		this.cible = config.getConfigurationSection("cible");
		this.faction = config.getConfigurationSection("faction");
		this.rarity = config.getConfigurationSection("rarity");
		this.type = config.getConfigurationSection("type");
		this.sort = config.getConfigurationSection("sort");
		this.feature = config.getConfigurationSection("feature");
	}
	
	public String cibleToString(CardCible cible) {
		return this.enumToString(cible, this.cible);
	}
	
	public String factionToString(CardFaction faction) {
		return faction.getColor()+this.enumToString(faction, this.faction);
	}
	
	public String rarityToString(CardRarity rarity) {
		return rarity.getColor()+this.enumToString(rarity, this.rarity);
	}
	
	public String typeToString(CardType type) {
		return this.enumToString(type, this.type);
	}
	
	public String sortToString(CardComparator sort) {
		return this.enumToString(sort, this.sort);
	}
	
	public String featureToString(CardFeatureType featureType, CardValue value) {
		if ((featureType == CardFeatureType.BOOST_DAMAGE
				|| featureType == CardFeatureType.BOOST_HEALTH
				|| featureType == CardFeatureType.BOOST_MANA
				|| featureType == CardFeatureType.REMOVE_MANA_HERO)
			&& value.asNumber() < 0)
			return this.enumToStringNeg(featureType, feature);
		else
			return this.enumToString(featureType, feature);
	}
	
	private String enumToString(Enum<?> enum1, ConfigurationSection config) {
		return config.getString(enum1.name().toLowerCase());
	}
	
	private String enumToStringNeg(Enum<?> enum1, ConfigurationSection config) {
		return config.getString(enum1.name().toLowerCase()+"_neg");
	}
	
}