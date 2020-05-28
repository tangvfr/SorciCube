package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class CartEntity extends Cart {

	protected int health;
	//protected ? skin;
	
	public CartEntity(String id,
			MaterialData material,
			String name,
			String[] description,
			int countMana,
			int damage,
			CartRarity rarity,
			CartFaction faction,
			int health) {
		super(id, material, name, description, countMana, damage, CartType.ENTITY, rarity, faction);
		this.health = health;
	}

	public int getHealth() {
		return this.health;
	}
	
}
