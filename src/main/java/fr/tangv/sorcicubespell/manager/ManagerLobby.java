package fr.tangv.sorcicubespell.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		} else {
			player.teleport(locationTuto);
		}
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
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(19);
		player.setMaxHealth(20);
		player.setHealth(20);
		teleportPlayerToSpawn(player);
		Bukkit.getScheduler().runTaskAsynchronously(sorci, new Runnable() {
			@Override
			public void run() {
				if (sorci.getManagerPlayers().containtPlayer(player))
					player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
				else
					player.sendMessage(sorci.getMessage().getString("message_welcom"));
			}
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(sorci.getParameter().getString("quit_message").replace("{player}", player.getDisplayName()));
	}
	
}
