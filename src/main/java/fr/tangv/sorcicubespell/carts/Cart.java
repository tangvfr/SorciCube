package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public abstract class Cart {
	
	protected String id;
	protected MaterialData material;
	protected String name;
	protected String[] description;
	protected int countMana;
	protected int damage;
	protected CartType type;
	protected CartRarity rarity;
	protected CartFaction faction;
	
	public Cart(String id,
			MaterialData material,
			String name,
			String[] description,
			int countMana,
			int damage,
			CartType type,
			CartRarity rarity,
			CartFaction faction) {
		this.id = id;
		this.material = material;
		this.name = name;
		this.description = description;
		this.countMana = countMana;
		this.damage = damage;
		this.type = type;
		this.rarity = rarity;
		this.faction = faction;
	}
	
	public String getId() {
		return id;
	}
	
	public MaterialData getMaterial () {
		return this.material;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[] getDescription() {
		return this.description;
	}
	
	public int getCountMana() {
		return this.countMana;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public CartType getType() {
		return this.type;
	}
	
	public CartRarity getRarity() {
		return this.rarity;
	}
	
	public CartFaction getFaction() {
		return this.faction;
	}
	
}
