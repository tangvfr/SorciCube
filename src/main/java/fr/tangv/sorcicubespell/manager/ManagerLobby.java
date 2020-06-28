package fr.tangv.sorcicubespell.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class ManagerLobby implements Listener {

	private SorciCubeSpell sorci;
	private Location locationTuto;
	private Location locationSpawn;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locationTuto = (Location) sorci.getParameter().get("location_tuto");
		this.locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private void teleportPlayerToSpawn(Player player) {
		if (sorci.getManagerPlayers().containtPlayer(player)) {
			player.teleport(locationSpawn);
			player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
		} else {
			player.teleport(locationTuto);
			player.sendMessage(sorci.getMessage().getString("message_welcom"));
		}
	}
	
	@EventHandler
	public void onFaim(FoodLevelChangeEvent e) {
		e.setFoodLevel(18);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onNoDammage(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (e.getTo().getY() < 0)
			teleportPlayerToSpawn(e.getPlayer());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		teleportPlayerToSpawn(e.getPlayer());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage(sorci.getParameter().getString("join_message").replace("{player}", player.getDisplayName()));
		teleportPlayerToSpawn(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(sorci.getParameter().getString("quit_message").replace("{player}", player.getDisplayName()));
	}
	
}
