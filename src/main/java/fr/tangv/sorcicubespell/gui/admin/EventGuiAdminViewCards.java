package fr.tangv.sorcicubespell.gui.admin;

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

import fr.tangv.sorcicubespell.manager.ManagerGuiAdmin;

public class EventGuiAdminViewCards implements Listener{

	private ManagerGuiAdmin manager;
	
	public EventGuiAdminViewCards(ManagerGuiAdmin manager) {
		this.manager = manager;
		for (Player player : Bukkit.getOnlinePlayers())
			this.manager.getPlayerGuiAdmins().put(player, new PlayerGuiAdmin(player));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.manager.getPlayerGuiAdmins().put(e.getPlayer(), new PlayerGuiAdmin(e.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.manager.getPlayerGuiAdmins().remove(e.getPlayer());
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerGuiAdmin player = this.manager.getPlayerGuiAdmins().get((Player) e.getWhoClicked());
			AbstractGuiAdmin gui = player.getGui();
			if (gui != null && e.getInventory() == player.getInvOfGui())
				gui.onDrag(player.getPlayer(), e);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerGuiAdmin player = this.manager.getPlayerGuiAdmins().get((Player) e.getWhoClicked());
			AbstractGuiAdmin gui = player.getGui();
			if (gui != null && e.getInventory() == player.getInvOfGui())
				gui.onClick(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerGuiAdmin player = this.manager.getPlayerGuiAdmins().get((Player) e.getPlayer());
			AbstractGuiAdmin gui = player.getGui();
			if (gui != null && e.getInventory() == player.getInvOfGui())
				gui.onClose(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerGuiAdmin player = this.manager.getPlayerGuiAdmins().get((Player) e.getPlayer());
			AbstractGuiAdmin gui = player.getGui();
			if (gui != null && e.getInventory() == player.getInvOfGui())
				gui.onOpen(player.getPlayer(), e);
		}
	}
	
}
