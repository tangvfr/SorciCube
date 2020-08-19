package fr.tangv.sorcicubespell.gui;

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

import fr.tangv.sorcicubespell.manager.ManagerGui;

public class EventGuiPlayer implements Listener{

	private ManagerGui manager;
	
	public EventGuiPlayer(ManagerGui manager) {
		this.manager = manager;
		for (Player player : Bukkit.getOnlinePlayers())
			this.manager.putPlayerGui(player, new PlayerGui(player));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.manager.putPlayerGui(e.getPlayer(), new PlayerGui(e.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.manager.removePlayerGui(e.getPlayer());
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerGui player = this.manager.getPlayerGui((Player) e.getWhoClicked());
			AbstractGui gui = player.getGui();
			if (gui != null && e.getInventory().hashCode() == player.getInvOfGui().hashCode())
				gui.onDrag(player.getPlayer(), e);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerGui player = this.manager.getPlayerGui((Player) e.getWhoClicked());
			AbstractGui gui = player.getGui();
			if (gui != null && e.getInventory().hashCode() == player.getInvOfGui().hashCode())
				gui.onClick(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerGui player = this.manager.getPlayerGui((Player) e.getPlayer());
			AbstractGui gui = player.getGui();
			if (gui != null && e.getInventory().hashCode() == player.getInvOfGui().hashCode())
				gui.onClose(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerGui player = this.manager.getPlayerGui((Player) e.getPlayer());
			AbstractGui gui = player.getGui();
			if (gui != null && e.getInventory().hashCode() == player.getInvOfGui().hashCode())
				gui.onOpen(player.getPlayer(), e);
		}
	}
	
}
