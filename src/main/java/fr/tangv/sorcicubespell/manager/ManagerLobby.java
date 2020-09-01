package fr.tangv.sorcicubespell.manager;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerLobby implements Listener {

	private SorciCubeSpell sorci;
	private Location locationTuto;
	private Location locationSpawn;
	private String formatChat;
	private ConcurrentHashMap<UUID, Byte> levelPlayers;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.levelPlayers = new ConcurrentHashMap<UUID, Byte>();
		this.locationTuto = (Location) sorci.getParameter().get("location_tuto");
		this.locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		this.formatChat = sorci.getParameter().getString("chat_format");
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setFormat(formatChat
				.replace("{level}", levelPlayers.get(e.getPlayer().getUniqueId()).toString())
				.replace("{displayname}", e.getPlayer().getDisplayName())
				.replace("{message}", e.getMessage())
			);
	}
	
	public void initPlayerLevel(Player player) throws Exception {
		PlayerFeature feature = sorci.getManagerPlayers().getPlayerFeature(player.getUniqueId());
		if (levelPlayers.containsKey(feature.getUUID()))
			levelPlayers.replace(feature.getUUID(), feature.getLevel());
		else
			levelPlayers.put(feature.getUUID(), feature.getLevel());
		player.setLevel(feature.getLevel());
		Config lc = sorci.getLevelConfig();
		if (!feature.isLevel((byte) lc.getInt("level_max"))) {
			int expNextLevel = lc.getInt("level_experience."+(feature.getLevel()+1)+".experience");
			player.setExp(feature.getExperience()/(float) expNextLevel);
		} else {
			player.setExp(1.0F);
		}
	}
	
	private void teleportPlayerToSpawn(Player player) {
		Location loc = sorci.getManagerCreatorFight().getLocationFor(player);
		if (loc == null) {
			if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId())) {
				loc = locationSpawn;
			} else {
				loc = locationTuto;
			}
		}
		player.teleport(loc);
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
		player.setCollidable(false);
		FightData fightData = sorci.getManagerFightData().getFightDataPlayer(player.getUniqueId());
		if (fightData != null && fightData.getStat() == FightStat.START) {
			sorci.sendPlayerToServer(player, fightData.getServer());
		} else {
			Bukkit.getScheduler().runTaskLater(sorci, new Runnable() {
				@Override
				public void run() {
					teleportPlayerToSpawn(player);
					if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId())) {
						player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
						try {
							initPlayerLevel(player);
						} catch (Exception e) {
							Bukkit.getLogger().warning(RenderException.renderException(e));
						}
					} else {
						player.sendMessage(sorci.getMessage().getString("message_welcom"));
						player.setLevel(0);
						player.setExp(0F);
					}
				}
			}, 1);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (levelPlayers.containsKey(player.getUniqueId()))
			levelPlayers.remove(player.getUniqueId());
		e.setQuitMessage(sorci.getParameter().getString("quit_message").replace("{player}", player.getDisplayName()));
		sorci.getManagerCreatorFight().playerLeave(player, true);
	}
	
}
