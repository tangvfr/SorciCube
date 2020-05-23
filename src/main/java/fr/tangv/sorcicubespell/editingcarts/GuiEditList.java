package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class GuiEditList extends GuiEdit {

	public GuiEditList(EditCartsGui ec, ConfigurationSection config) {
		super(ec, config.getString("name"));
		
	}

	@Override
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, this.name);
		
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {
		
	}
	
}
