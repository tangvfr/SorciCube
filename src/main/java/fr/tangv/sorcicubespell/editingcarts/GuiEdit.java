package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import fr.tangv.sorcicubespell.util.Gui;

public abstract class GuiEdit implements Gui {

	protected EditCartsGui ec;
	protected String name;
	
	public GuiEdit(EditCartsGui ec, String name) {
		this.ec = ec;
		this.name = name;
	}

	@Override
	public void open(Player player) {
		ec.editingCarts.get(player).setGui(this);
		player.openInventory(this.getInventory(player));
	}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		ec.editingCarts.get(player).setGui(null);
	}

	@Override
	public void onOpen(Player player, InventoryOpenEvent e) {}
	
	@Override
	public String getName() {
		return name;
	}

}
