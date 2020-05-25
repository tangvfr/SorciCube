package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tangv.sorcicubespell.util.Gui;

public class EventEditCartsGui implements Listener{

	private EditCartsGui ec;
	
	public EventEditCartsGui(EditCartsGui ec) {
		this.ec = ec;
		for (Player player : Bukkit.getOnlinePlayers())
			this.ec.editingCarts.put(player, new PlayerEditCart(player));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.ec.editingCarts.put(e.getPlayer(), new PlayerEditCart(e.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.ec.editingCarts.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getWhoClicked());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onDrag(player.getPlayer(), e);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getWhoClicked());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onClick(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getPlayer());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onClose(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getPlayer());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onOpen(player.getPlayer(), e);
		}
	}
	
}
