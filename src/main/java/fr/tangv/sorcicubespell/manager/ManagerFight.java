package fr.tangv.sorcicubespell.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class ManagerFight implements Listener {

	private SorciCubeSpell sorci;
	
	public ManagerFight(SorciCubeSpell sorci) {
		this.sorci = sorci;
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private void teleportPlayerToLoc(Player player) {
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (e.getTo().getY() < 0)
			teleportPlayerToLoc(e.getPlayer());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		teleportPlayerToLoc(e.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("");
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(20);
		teleportPlayerToLoc(player);
		//tempo add fight
		player.setMaxHealth(20);
		player.setHealth(20);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
	}
	
}
