package fr.tangv.sorcicubespell.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.configs.BasicGuiConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.manager.ManagerGui;

public abstract class AbstractGui<T extends BasicGuiConfig> implements Gui {

	protected ManagerGui manager;
	protected T config;
	protected SorciCubeSpell sorci;
	
	public AbstractGui(ManagerGui manager, T config) {
		this.manager = manager;
		this.config = config;
		this.sorci = manager.getSorci();
	}
	
	public PlayerGui getPlayerGui(Player player) {
		return manager.getPlayerGui(player);
	}
	
	@Override
	public void open(Player player) {
		PlayerGui playerGA = getPlayerGui(player);
		playerGA.setGui(this);
		Inventory inv = this.createInventory(player);
		playerGA.setInvOfGui(inv);
		player.openInventory(inv);
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		PlayerGui playerGA = getPlayerGui(player);
		playerGA.setGui(null);
	}

	@Override
	public void onOpen(Player player, InventoryOpenEvent e) {}
	
	@Override
	public String getName() {
		return config.name.value;
	}

}
