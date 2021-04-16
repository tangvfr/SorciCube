package fr.tangv.sorcicubespell.util;

import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue;
import fr.tangv.sorcicubecore.configs.CibleEnumConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class EnumTool {

	private final SorciCubeSpell sorci;
	
	public EnumTool(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	public String cibleToString(CardCible cible) {
		CibleEnumConfig c = sorci.config().enums.cible;
		switch (cible) {
			case ALL:
				return c.all.value;
				
			case ALL_ALLY:
				return c.allAlly.value;
				
			case ALL_ENEMIE:
				return c.allEnemie.value;
				
			case ALL_ENTITY:
				return c.allEntity.value;
				
			case ALL_ENTITY_ALLY:
				return c.allEntityAlly.value;
				
			case ALL_ENTITY_ENEMIE:
				return c.allEntityEnemie.value;
				
			case ALL_HERO:
				return c.allHero.value;
				
			case HERO_ALLY:
				return c.heroAlly.value;
				
			case HERO_ENEMIE:
				return c.allEntityEnemie.value;
				
			case NONE:
				return c.none.value;
				
			case ONE:
				return c.one.value;
				
			case ONE_ALLY:
				return c.oneAlly.value;
				
			case ONE_ENEMIE:
				return c.oneEnemie.value;
				
			case ONE_ENTITY:
				return c.oneEntity.value;
				
			case ONE_ENTITY_ALLY:
				return c.oneEntityAlly.value;
				
			case ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE:
				return c.oneEntityAllyAndOneEntityEnemie.value;
				
			case ONE_ENTITY_ENEMIE:
				return c.oneEntityEnemie.value;
				
			case ONE_HERO:
				return c.oneHero.value;
		}
		return null;
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
