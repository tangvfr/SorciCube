package fr.tangv.sorcicubespell.carts;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import fr.tangv.sorcicubespell.util.ItemBuild;

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

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack toItem(int amount, boolean ench) {
		ArrayList<String> lore = new ArrayList<String>();
		//type
		lore.add("");
		lore.add("§7"+this.type.name());
		lore.add("");
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
