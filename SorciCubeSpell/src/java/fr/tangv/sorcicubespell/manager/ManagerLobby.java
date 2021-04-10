package fr.tangv.sorcicubespell.manager;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightStat;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class ManagerLobby implements Listener {

	private final SorciCubeSpell sorci;
	private final Location locationTuto;
	private final Location locationSpawn;
	private final String formatChat;
	private final String noneLvl;
	private final String noneGroup;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.noneLvl = sorci.getParameter().getString("none_lvl");
		this.noneGroup = sorci.getParameter().getString("none_group");
		this.locationTuto = (Location) sorci.getParameter().get("location_tuto");
		this.locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		this.formatChat = sorci.getParameter().getString("chat_format");
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private boolean isAuto(Player player) {
		return player.getGameMode() == GameMode.CREATIVE && player.hasPermission("sorcicubespell.build");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		PlayerGui player = sorci.getManagerGui().getPlayerGui(e.getPlayer());
		if (player != null) {
			PlayerFeatures feature = player.getPlayerFeatures();
			e.setFormat(formatChat
					.replace("{group}", player.getDisplayGroup())
					.replace("{displayname}", e.getPlayer().getDisplayName())
					.replace("{message}", e.getMessage())
					.replace("{level}", Byte.toString(feature.getLevel()))
				);
		} else {
			e.setFormat(formatChat
					.replace("{group}", noneGroup)
					.replace("{displayname}", e.getPlayer().getDisplayName())
					.replace("{message}", e.getMessage())
					.replace("{level}", noneLvl)
				);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!isAuto(e.getPlayer()))
			if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				e.setUseItemInHand(Result.DEFAULT);
				e.setUseInteractedBlock(Result.DENY);
			} else {
				e.setCancelled(true);
			}
	}
	
	private void teleportPlayerToSpawn(Player player) {
		Location loc = sorci.getManagerCreatorFight().getLocationFor(player);
		if (loc == null) {
			if (sorci.getManagerGui().getPlayerGui(player).getPlayerFeatures() != null) {
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
				try {
					FightData fightData = sorci.getHandlerFightData().getFightDataPlayer(player.getUniqueId());
					if (fightData != null && fightData.getStat() == FightStat.START) {
						sorci.sendPlayerToServer(player, fightData.getServer());
					} else {
						if (sorci.getHandlerPlayers().existPlayer(player.getUniqueId())) {
							player.sendMessage(sorci.getMessage().getString("message_welcom_back"));
							try {
								PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
								playerG.setPlayerFeatures(
										sorci.getHandlerPlayers().getPlayer(player.getUniqueId(), player.getName()),
										sorci
								);
								sorci.getManagerGui().updateDisplayPlayer(playerG);
							} catch (Exception e) {
								Bukkit.getLogger().throwing("ManagerLobby", "onJoin", e);
							}
						} else {
							player.sendMessage(sorci.getMessage().getString("message_welcom"));
							player.setLevel(0);
							player.setExp(0F);
						}
						teleportPlayerToSpawn(player);
					}
				} catch (IOException | ReponseRequestException | RequestException e) {
					e.printStackTrace();
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
