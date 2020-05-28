package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class CartSort extends Cart {

	private int heal;
	private int giveMana;
	private CartCible cible;
	
	public CartSort(String id,
			MaterialData material,
			String name,
			String[] description,
			int countMana,
			int damage,
			CartRarity rarity,
			CartFaction faction,
			int heal,
			int giveMana,
			CartCible cible) {
		super(id, material, name, description, countMana, damage, CartType.SORT, rarity, faction);
		this.setHeal(heal);
		this.setGiveMana(giveMana);
		this.setCible(cible);
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}

	public int getGiveMana() {
		return giveMana;
	}

	public void setGiveMana(int giveMana) {
		this.giveMana = giveMana;
	}

	public CartCible getCible() {
		return cible;
	}

	public void setCible(CartCible cible) {
		this.cible = cible;
	}
	
}
