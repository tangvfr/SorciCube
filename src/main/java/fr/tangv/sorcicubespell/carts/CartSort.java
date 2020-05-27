package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import fr.tangv.sorcicubespell.util.ItemBuild;

public class CartSort extends Cart {

	protected int heal;
	protected int giveMana;
	protected CartCible cible;
	
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
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack toItem(int amount, boolean ench) {
		ArrayList<String> lore = new ArrayList<String>();
		//type
		lore.add("");
		lore.add("§7"+this.type.name());
		lore.add("");
		lore.add("§7Id: "+id);
		//lore
		for (int i = 0; i < this.description.length; i++)
			lore.add(this.description[i]);
		//return item
		return ItemBuild.buildItem(this.material.getItemType(),
				amount,
				(short) 0,
				this.material.getData(),
				this.name,
				lore,
				ench);
	}
	
}
