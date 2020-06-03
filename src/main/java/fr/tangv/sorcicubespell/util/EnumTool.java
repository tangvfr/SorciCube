package fr.tangv.sorcicubespell.util;

import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartComparator;
import fr.tangv.sorcicubespell.carts.CartFaction;
import fr.tangv.sorcicubespell.carts.CartRarity;
import fr.tangv.sorcicubespell.carts.CartType;

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
	
	public String cibleToString(CartCible cible) {
		return this.enumToString(cible, this.cible);
	}
	
	public String factionToString(CartFaction faction) {
		return this.enumToString(faction, this.faction);
	}
	
	public String rarityToString(CartRarity rarity) {
		return this.enumToString(rarity, this.rarity);
	}
	
	public String typeToString(CartType type) {
		return this.enumToString(type, this.type);
	}
	
	public String sortToString(CartComparator sort) {
		return this.enumToString(sort, this.sort);
	}
	
	public String enumToString(Enum<?> enum1, ConfigurationSection config) {
		return config.getString(enum1.name().toLowerCase());
	}
	
}
