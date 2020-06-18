package fr.tangv.sorcicubespell.util;

import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubespell.cards.CardCible;
import fr.tangv.sorcicubespell.cards.CardComparator;
import fr.tangv.sorcicubespell.cards.CardFaction;
import fr.tangv.sorcicubespell.cards.CardRarity;
import fr.tangv.sorcicubespell.cards.CardType;

public class EnumTool {

	private ConfigurationSection cible;
	private ConfigurationSection faction;
	private ConfigurationSection rarity;
	private ConfigurationSection type;
	private ConfigurationSection sort;
	
	public EnumTool(Config config) {
		this.cible = config.getConfigurationSection("cible");
		this.faction = config.getConfigurationSection("faction");
		this.rarity = config.getConfigurationSection("rarity");
		this.type = config.getConfigurationSection("type");
		this.sort = config.getConfigurationSection("sort");
	}
	
	public String cibleToString(CardCible cible) {
		return this.enumToString(cible, this.cible);
	}
	
	public String factionToString(CardFaction faction) {
		return this.enumToString(faction, this.faction);
	}
	
	public String rarityToString(CardRarity rarity) {
		return this.enumToString(rarity, this.rarity);
	}
	
	public String typeToString(CardType type) {
		return this.enumToString(type, this.type);
	}
	
	public String sortToString(CardComparator sort) {
		return this.enumToString(sort, this.sort);
	}
	
	public String enumToString(Enum<?> enum1, ConfigurationSection config) {
		return config.getString(enum1.name().toLowerCase());
	}
	
}
