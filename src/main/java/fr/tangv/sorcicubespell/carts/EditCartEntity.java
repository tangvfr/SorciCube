package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class EditCartEntity extends CartEntity {

	public EditCartEntity(CartEntity cart) {
		super(cart.getId(), 
				cart.getMaterial(),
				cart.getName(),
				cart.getDescription(),
				cart.getCountMana(),
				cart.getDamage(),
				cart.getRarity(),
				cart.getFaction(),
				cart.getHealth());
	}
	
	public void setMaterial(MaterialData material) {
		this.material = material;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String[] description) {
		this.description = description;
	}
	
	public void setCountMana(int countMana) {
		this.countMana = countMana;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setRarity(CartRarity rarity) {
		this.rarity = rarity;
	}
	
	public void setFaction(CartFaction faction) {
		this.faction = faction;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
}