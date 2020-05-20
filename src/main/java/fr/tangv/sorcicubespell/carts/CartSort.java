package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public class CartSort extends Cart {

	protected int heal;
	protected int giveMana;
	protected CartCible cible;
	
	public CartSort(int id,
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
		this.heal = heal;
		this.giveMana = giveMana;
		this.cible = cible;
	}
	
	public int getHeal() {
		return this.heal;
	}
	
	public int getGiveMana() {
		return this.giveMana;
	}
	
	public CartCible getCible() {
		return this.cible;
	}
	
}
