package fr.tangv.sorcicubespell.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class ManagerLobby implements Listener {

	private SorciCubeSpell sorci;
	private Location locationTuto;
	private Location locationSpawn;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locationTuto = (Location) sorci.getParameter().get("location_tuto");
		this.locationSpawn = (Location) sorci.getParameter().get("location_spawn");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage(sorci.getParameter().getString("join_message").replace("{player}", player.getDisplayName()));
		if (sorci.getManagerPlayers().containtPlayer(player)) {
			player.teleport(locationSpawn);
			player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
		} else {
			player.teleport(locationTuto);
			player.sendMessage(sorci.getMessage().getString("message_welcom"));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(sorci.getParameter().getString("quit_message").replace("{player}", player.getDisplayName()));
	}
	
}
