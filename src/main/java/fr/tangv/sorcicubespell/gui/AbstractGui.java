package fr.tangv.sorcicubespell.gui;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.Gui;

public abstract class AbstractGui implements Gui {

	protected ManagerGui manager;
	protected ConfigurationSection config;
	protected String name;
	
	public AbstractGui(ManagerGui manager, ConfigurationSection config) {
		this.manager = manager;
		this.name = config.getString("name");
		this.config = config;
	}

	public PlayerGui getPlayerGui(Player player) {
		return manager.getPlayerGuis().get(player);
	}
	
	@Override
	public void open(Player player) {
		PlayerGui playerGA = getPlayerGui(player);
		playerGA.setGuiAdmin(this);
		Inventory inv = this.getInventory(player);
		playerGA.setInvOfGui(inv);
		player.openInventory(inv);
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		PlayerGui playerGA = getPlayerGui(player);
		playerGA.setGuiAdmin(null);
	}

	@Override
	public void onOpen(Player player, InventoryOpenEvent e) {}
	
	@Override
	public String getName() {
		return this.name;
	}

}
