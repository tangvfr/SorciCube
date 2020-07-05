package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tangv.sorcicubespell.manager.ManagerFight;

public class EventFight implements Listener {

	private ManagerFight manager;

	public EventFight(ManagerFight manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e) {
		if (manager.getPlayerFights().contains(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		for (PreFight preFight : manager.getPreFights().values())
			if (preFight.getPlayerUUID1().equals(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				return;
			}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (manager.getPlayerFights().contains(e.getPlayer())) {
			e.getPlayer().openInventory(e.getPlayer().getInventory());
		}
		e.setCancelled(false);
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (manager.getPlayerFights().contains(e.getWhoClicked())) {
			PlayerFight player = manager.getPlayerFights().get(e.getWhoClicked());
			if (player.canPlay()) {
				//action here
				//and detect where click
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("");
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setCollidable(false);
		player.getInventory().clear();
		for (Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
			player.hidePlayer(other);
		}
		manager.playerJoin(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		manager.playerQuit(e.getPlayer());
	}
	
}
