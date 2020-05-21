package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class EditCartSort extends CartSort {

	public EditCartSort(CartSort cart) {
		super(cart.getId(), 
				cart.getMaterial(),
				cart.getName(),
				cart.getDescription(),
				cart.getCountMana(),
				cart.getDamage(),
				cart.getRarity(),
				cart.getFaction(),
				cart.getHeal(),
				cart.getGiveMana(),
				cart.getCible());
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
	
	public void setHeal(int heal) {
		this.heal = heal;
	}
	
	public void setGiveMana(int giveMana) {
		this.giveMana = giveMana;
	}
	
	public void setCible(CartCible cible) {
		this.cible = cible;
	}
	
}
