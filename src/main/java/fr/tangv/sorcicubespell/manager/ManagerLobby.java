package fr.tangv.sorcicubespell.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerLobby implements Listener {

	private SorciCubeSpell sorci;
	private Location locationTuto;
	private Location locationSpawn;
	private String formatChat;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locationTuto = (Location) sorci.getParameter().get("location_tuto");
		this.locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		this.formatChat = sorci.getParameter().getString("chat_format");
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setFormat(formatChat
				.replace("{displayname}", e.getPlayer().getDisplayName())
				.replace("{message}", e.getMessage())
				.replace("{level}", Byte.toString(sorci.getManagerGui().getPlayerGui(e.getPlayer()).getPlayerFeature().getLevel()))
			);
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
		Bukkit.getScheduler().runTaskLater(sorci, new Runnable() {
			@Override
			public void run() {
				FightData fightData = sorci.getManagerFightData().getFightDataPlayer(player.getUniqueId());
				if (fightData != null && fightData.getStat() == FightStat.START) {
					sorci.sendPlayerToServer(player, fightData.getServer());
				} else {
					teleportPlayerToSpawn(player);
					if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId())) {
						player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
						try {
							PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
							playerG.setPlayerFeature(
									sorci.getManagerPlayers().getPlayerFeature(player.getUniqueId())
							);
							sorci.getManagerGui().updateDisplayPlayer(playerG);
						} catch (Exception e) {
							Bukkit.getLogger().warning(RenderException.renderException(e));
						}
					} else {
						player.sendMessage(sorci.getMessage().getString("message_welcom"));
						player.setLevel(0);
						player.setExp(0F);
					}
				}
			}
		}, 1);
	}
	
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if (e.getFoodLevel() < 19) {
			e.setFoodLevel(19);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(sorci.getParameter().getString("quit_message").replace("{player}", player.getDisplayName()));
		sorci.getManagerCreatorFight().playerLeave(player, true);
	}
	
}
