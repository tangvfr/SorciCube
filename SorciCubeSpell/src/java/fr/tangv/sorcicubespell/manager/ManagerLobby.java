package fr.tangv.sorcicubespell.manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.NameTag;

public class ManagerLobby implements Listener {

	private final SorciCubeSpell sorci;
	private final Location locationTuto;
	private final Location locationSpawn;
	
	public ManagerLobby(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locationTuto = SorciCubeSpell.convertLocation(sorci.config().locations.locationTuto);
		this.locationSpawn = SorciCubeSpell.convertLocation(sorci.config().locations.locationSpawn);
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private boolean isAuto(Player player) {
		return player.getGameMode() == GameMode.CREATIVE && player.hasPermission("sorcicubespell.build");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		PlayerGui player = sorci.getManagerGui().getPlayerGui(e.getPlayer());
		e.setFormat(
				player.getDataPlayer()
				.replace(sorci.config().parameter.chatFormat.value)
				.replace("{message}", e.getMessage()
			));
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
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) throws IOException, ResponseRequestException, RequestException, DeckException {
		Player player = e.getPlayer();
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(19);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setCollidable(false);
		FightData fightData = sorci.getHandlerFightData().getFightDataPlayer(player.getUniqueId());
		if (fightData != null && fightData.getStat() == FightStat.START) {
			sorci.sendPlayerToServer(player, fightData.getServer());
		} else {
			PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
			if (sorci.getHandlerPlayers().existPlayer(player.getUniqueId())) {
				player.sendMessage(sorci.config().messages.welcomBack.value);
				playerG.setPlayerFeatures(
						sorci.getHandlerPlayers().getPlayer(player.getUniqueId(), player.getName()),
						sorci
				);
				List<Player> pl = Arrays.asList(player);
				for (PlayerGui playerGui : sorci.getManagerGui().valuesPlayerGui())
					if (playerGui.getPlayerFeatures() != null)
						NameTag.sendNameTag(playerGui.getDataPlayer(), pl);
				sorci.getManagerGui().updateDisplayPlayer(playerG);
			} else {
				NameTag.sendNameTag(playerG.getDataPlayer(), Bukkit.getOnlinePlayers());
				player.sendMessage(sorci.config().messages.welcom.value);
				player.setLevel(0);
				player.setExp(0F);
			}
			teleportPlayerToSpawn(player);
		}
		e.setJoinMessage(sorci.getManagerGui().getPlayerGui(player).getDataPlayer().replace(sorci.config().parameter.joinMessage.value));
		/*Bukkit.getScheduler().runTaskLater(sorci, new Runnable() {
			@Override
			public void run() {
				try {
					
				} catch (IOException | ResponseRequestException | RequestException e) {
					e.printStackTrace();
				}
			}
		}, 1);*/
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
		e.setQuitMessage(sorci.getManagerGui().getPlayerGui(player).getDataPlayer().replace(sorci.config().parameter.quitMessage.value));
		sorci.getManagerCreatorFight().playerLeave(player, true);
	}
	
}
