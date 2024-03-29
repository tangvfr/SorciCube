package fr.tangv.sorcicubespell.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.configs.ArenaConfig;
import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightStat;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.EventFight;
import fr.tangv.sorcicubespell.fight.Fight;
import fr.tangv.sorcicubespell.fight.FightArena;
import fr.tangv.sorcicubespell.fight.FightSpectator;
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.ValueFight;
import fr.tangv.sorcicubespell.player.DataPlayer;

public class ManagerFight implements Runnable {
	
	private final SorciCubeSpell sorci;
	private final ConcurrentHashMap<UUID, PreFight> preFights;
	private final ConcurrentHashMap<UUID, Fight> fights;
	private final Vector<FightArena> arena;
	private final ConcurrentHashMap<UUID, FightSpectator> playerInstance;
	
	public ManagerFight(SorciCubeSpell sorci) throws Exception {
		this.sorci = sorci;
		ValueFight.V = new ValueFight(sorci.config());
		this.playerInstance = new ConcurrentHashMap<UUID, FightSpectator>();
		this.preFights = new ConcurrentHashMap<UUID, PreFight>();
		this.fights = new ConcurrentHashMap<UUID, Fight>();
		this.arena = new Vector<FightArena>();
		for (ArenaConfig arena : sorci.config().arenas)
			if (arena.isEnable.value)
				this.arena.add(new FightArena(arena));
		if (arena.size() <= 0)
			throw new Exception("Nothing arena !");
		//event
		Bukkit.getPluginManager().registerEvents(new EventFight(this), sorci);
		Bukkit.getScheduler().runTaskTimerAsynchronously(sorci, this, 0, 5);
	}
	
	public boolean isSpectator(UUID uuid) {
		return playerInstance.containsKey(uuid);
	}
	
	public FightSpectator getSpectator(UUID uuid) {
		return playerInstance.get(uuid);
	}
	
	public boolean inPreFight(UUID uuid) {
		for (PreFight preFight : preFights.values())
			if (preFight.getPlayerUUID1().equals(uuid)) {
				return true;
			}
		return false;
	}
	
	public FightArena pickArena() {
		return arena.elementAt((int) (arena.size()*Math.random()));
	}
	
	public void playerQuit(Player player) {
		UUID uuid = player.getUniqueId();
		if (playerInstance.containsKey(uuid)) {
			FightSpectator spectator = playerInstance.get(uuid);
			spectator.removeInBossBar();
			if (!spectator.isFightPlayer()) {
				spectator.fight.removeSpectator(spectator);
				playerInstance.remove(uuid);
			}
		}
	}
	
	public void playerJoin(Player player) throws IOException, ResponseRequestException, RequestException, DeckException {
		boolean kick = true;
		PlayerFeatures features = sorci.getHandlerPlayers().getPlayer(player.getUniqueId(), player.getName());
		Group group = sorci.getManagerPermissions().applyPermission(player, features.isAdmin(), features.getGroup());
		DataPlayer dataPlayer = new DataPlayer(features, group, sorci.config().parameter);
		if (playerInstance.containsKey(player.getUniqueId())) {
			FightSpectator spectator = playerInstance.get(player.getUniqueId());
			if (spectator.isFightPlayer() && !spectator.fight.isEnd()) {
				for (Player other : Bukkit.getOnlinePlayers()) {
						other.hidePlayer(player);
						player.hidePlayer(other);
				}
				((PlayerFight) spectator).newPlayer(player);
				kick = false;
			}
		} else if (preFights.containsKey(player.getUniqueId())) {
			PreFight preFight = preFights.get(player.getUniqueId());
			preFight.complet(player, dataPlayer, features);
			kick = false;
		} else {
			FightData fightData = sorci.getHandlerFightData().getFightDataPlayer(player.getUniqueId());
			if (fightData != null && fightData.getStat() == FightStat.WAITING) {
				PreFight preFight = PreFight.createPreFight(player, dataPlayer, features, fightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				fightData.setStat(FightStat.STARTING);
				sorci.getHandlerFightData().updateFightData(fightData);
				kick = false;
			}
		}
		if (kick) {
			UUID fightUUID = sorci.getHandlerFightData().whichSpectate(player.getUniqueId());
			if (fightUUID != null && fights.containsKey(fightUUID)) {
				Fight fight = fights.get(fightUUID);
				if (fight.isEnd()) {
					Bukkit.getScheduler().runTaskLaterAsynchronously(sorci, () -> {
						sendLobbyPlayer(player);
					}, 1);
					return;
				}
				for (Player other : Bukkit.getOnlinePlayers()) {
					other.hidePlayer(player);
					player.hidePlayer(other);
				}
				FightSpectator spectator = new FightSpectator(
						fight,
						player,
						fight.getPlayer1().getLocBase().clone().add(fight.getPlayer2().getLocBase()).multiply(0.5),
						true,
						dataPlayer
				);
				playerInstance.put(player.getUniqueId(), spectator);
				player.setAllowFlight(true);
				player.setFlying(true);
				spectator.initForViewFight();
				fight.addSpectator(spectator);
				spectator.initBarSpectator();
			} else if (player.hasPermission("sorcicubespell.join.fight")) {
				for (Player other : Bukkit.getOnlinePlayers())
					if (playerInstance.containsKey(other.getUniqueId()))
						other.hidePlayer(player);
			} else {
				Bukkit.getScheduler().runTaskLaterAsynchronously(sorci, () -> {
					sendLobbyPlayer(player);
				}, 1);
			}
		} else {
			for (Player other : Bukkit.getOnlinePlayers()) {
				other.hidePlayer(player);
				player.hidePlayer(other);
			}
		}
	}

	private void sendLobbyPlayer(Player player) {
		if (player.isOnline())
			sorci.sendPlayerToServer(player, sorci.getNameServerLobby());
	}
	
	@Override
	public void run() {
		for (PreFight preFight : new ArrayList<PreFight>(this.preFights.values())) {
			if (preFight.updateOutOfDate()) {
				preFights.remove(preFight.getPlayerUUID2());
				FightData fightData = preFight.getFightData();
				if (preFight.isComplet()) {
					if (!preFight.getPlayer1().isOnline() || !preFight.getPlayer2().isOnline()) {
						sendLobbyPlayer(preFight.getPlayer1());
						sendLobbyPlayer(preFight.getPlayer2());
					} else {
						Bukkit.getScheduler().runTask(sorci, () -> {
							try {
								Fight fight = new Fight(sorci, preFight);
								fights.put(fightData.getFightUUID(), fight);
								playerInstance.put(fight.getPlayer1().getUUID(), fight.getPlayer1());
								playerInstance.put(fight.getPlayer2().getUUID(), fight.getPlayer2());
								fightData.setStat(FightStat.START);
								sorci.getHandlerFightData().updateFightData(fightData);
							} catch (Exception e) {
								try {
									sorci.getHandlerFightData().removeFightDataFight(fightData);
								} catch (IOException | ResponseRequestException | RequestException e1) {
									e1.printStackTrace();
								}
								Bukkit.getLogger().throwing("ManagerFight" ,"run", e);
								sendLobbyPlayer(preFight.getPlayer1());
								sendLobbyPlayer(preFight.getPlayer2());
							}
						});
						continue;
					}
				} else {
					sendLobbyPlayer(preFight.getPlayer1());
				}
				try {
					sorci.getHandlerFightData().removeFightDataFight(fightData);
				} catch (IOException | ResponseRequestException | RequestException e) {
					e.printStackTrace();
				}
			}
		}
		for (Fight fight : new ArrayList<Fight>(this.fights.values()))
			try {
				fight.update();
				if (fight.isDeleted()) {
					sorci.getHandlerFightData().removeFightDataFight(fight.getFightData());
					playerInstance.remove(fight.getPlayer1().getUUID());
					playerInstance.remove(fight.getPlayer2().getUUID());
					for (FightSpectator spectator : fight.getSpectators())
						playerInstance.remove(spectator.getUUID());
					fights.remove(fight.getFightData().getFightUUID());
				}
			} catch (Exception e) {
				Bukkit.getLogger().throwing("ManagerFight" ,"run", e);
			}
	}
	
	public void stop() {
		try {
			sorci.getHandlerFightData().removeAllFightDataServer(sorci.getNameServer());
		} catch (IOException | ResponseRequestException | RequestException e) {
			e.printStackTrace();
		}
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
}
