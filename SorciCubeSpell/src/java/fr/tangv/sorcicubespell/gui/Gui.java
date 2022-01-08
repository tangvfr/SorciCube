package fr.tangv.sorcicubespell.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface Gui {

	public default void open(Player player) {
		player.openInventory(createInventory(player));
	}
	
	public Inventory createInventory(Player player);
	public void onDrag(Player player, InventoryDragEvent e);
	public void onClick(Player player, InventoryClickEvent e);
	public void onClose(Player player, InventoryCloseEvent e);
	public void onOpen(Player player, InventoryOpenEvent e);
	public String getName();
	
}
