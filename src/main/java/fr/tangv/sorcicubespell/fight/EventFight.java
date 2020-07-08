package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.tangv.sorcicubespell.manager.ManagerFight;

public class EventFight implements Listener {

	private ManagerFight manager;

	public EventFight(ManagerFight manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
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
	public void onSwap(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer()))
			manager.getPlayerFights().get(e.getPlayer()).openInvHistoric();
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		e.getWhoClicked().sendMessage("You in fight list: "+manager.getPlayerFights().containsKey(e.getWhoClicked()));
		if (manager.getPlayerFights().containsKey(e.getWhoClicked())) {
			PlayerFight player = manager.getPlayerFights().get(e.getWhoClicked());
			if (e.getInventory().hashCode() == player.getInvHistoric().hashCode()) {
				player.getPlayer().sendMessage("Click in Inv");
				if (player.canPlay()) {
					//action here
					//and detect where click
					player.getPlayer().sendMessage("Click raw: "+e.getRawSlot());
				}
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onOpenInv(InventoryOpenEvent e) {
		if (!(manager.getPlayerFights().containsKey(e.getPlayer())
			&& manager.getPlayerFights().get(e.getPlayer()).getInvHistoric().hashCode() ==  e.getInventory().hashCode())
			&& !e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
		}
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
		Bukkit.getScheduler().runTaskLater(manager.getSorci(), new Runnable() {
			@Override
			public void run() {
				manager.playerJoin(player);
			}
		}, 1);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		manager.playerQuit(e.getPlayer());
	}
	
}
