package fr.tangv.sorcicubespell.gui.admin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.manager.ManagerGuiAdmin;
import fr.tangv.sorcicubespell.util.Gui;

public abstract class AbstractGuiAdmin implements Gui {

	protected ManagerGuiAdmin manager;
	protected ConfigurationSection config;
	protected String name;
	
	public AbstractGuiAdmin(ManagerGuiAdmin manager, ConfigurationSection config) {
		this.manager = manager;
		this.name = config.getString("name");
		this.config = config;
	}

	public PlayerGuiAdmin getPlayerGuiAdmin(Player player) {
		return manager.getPlayerGuiAdmins().get(player);
	}
	
	@Override
	public void open(Player player) {
		PlayerGuiAdmin playerGA = getPlayerGuiAdmin(player);
		playerGA.setGuiAdmin(this);
		Inventory inv = this.getInventory(player);
		playerGA.setInvOfGui(inv);
		player.openInventory(inv);
	}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		PlayerGuiAdmin playerGA = getPlayerGuiAdmin(player);
		playerGA.setGuiAdmin(null);
	}

	@Override
	public void onOpen(Player player, InventoryOpenEvent e) {}
	
	@Override
	public String getName() {
		return this.name;
	}

}
