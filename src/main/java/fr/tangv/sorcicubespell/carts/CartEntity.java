package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class CartEntity extends Cart {

	private int health;
	private String skin;
	
	public CartEntity(String id,
			MaterialData material,
			String materialURL,
			String name,
			String[] description,
			int countMana,
			int damage,
			CartRarity rarity,
			CartFaction faction,
			int health,
			String skin) {
		super(id, material, materialURL, name, description, countMana, damage, CartType.ENTITY, rarity, faction);
		this.setHealth(health);
		this.setSkin(skin);
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
	
}
