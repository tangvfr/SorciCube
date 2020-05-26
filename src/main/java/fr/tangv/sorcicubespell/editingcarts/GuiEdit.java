package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import fr.tangv.sorcicubespell.util.Gui;

public abstract class GuiEdit implements Gui {

	protected EditCartsGui ec;
	protected ConfigurationSection config;
	protected String name;
	
	public GuiEdit(EditCartsGui ec, ConfigurationSection config) {
		this.ec = ec;
		this.name = config.getString("name");
		this.config = config;
	}

	@Override
	public void open(Player player) {
		this.ec.editingCarts.get(player).setGuiOpened(this);
		player.openInventory(this.getInventory(player));
	}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		this.ec.editingCarts.get(player).setGui(null);
	}

	@Override
	public void onOpen(Player player, InventoryOpenEvent e) {
		PlayerEditCart p = this.ec.editingCarts.get(player);
		p.setGui(p.getGuiOpened());
	}
	
	@Override
	public String getName() {
		return this.name;
	}

}
