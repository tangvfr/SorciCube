package fr.tangv.sorcicubespell.manager;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.EventFight;
import fr.tangv.sorcicubespell.fight.Fight;
import fr.tangv.sorcicubespell.fight.FightArena;
import fr.tangv.sorcicubespell.fight.FightSpectator;
import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.fight.ValueFight;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerFight implements Runnable {
	
	private final SorciCubeSpell sorci;
	private final ConcurrentHashMap<UUID, PreFight> preFights;
	private final ConcurrentHashMap<UUID, Fight> fights;
	private final Vector<FightArena> arena;
	private final ConcurrentHashMap<UUID, FightSpectator> playerInstance;
	
	public ManagerFight(SorciCubeSpell sorci) throws Exception {
		this.sorci = sorci;
		ValueFight.V = new ValueFight(sorci);
		this.playerInstance = new ConcurrentHashMap<UUID, FightSpectator>();
		this.preFights = new ConcurrentHashMap<UUID, PreFight>();
		this.fights = new ConcurrentHashMap<UUID, Fight>();
		this.arena = new Vector<FightArena>();
		for (String name : sorci.getArenaConfig().getKeys(false))
			this.arena.add(new FightArena(name, sorci.getArenaConfig().getConfigurationSection(name)));
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
				spectator.getFight().removeSpectator(spectator);
				playerInstance.remove(uuid);
			}
		}
	}
	
	public void playerJoin(Player player) {
		boolean kick = true;
		Bukkit.broadcastMessage("Player: "+player.getName());
		if (playerInstance.containsKey(player.getUniqueId())) {
			Bukkit.broadcastMessage("Already in game");
			FightSpectator spectator = playerInstance.get(player.getUniqueId());
			if (spectator.isFightPlayer() && !spectator.getFight().isEnd()) {
				for (Player other : Bukkit.getOnlinePlayers()) {
						other.hidePlayer(player);
						player.hidePlayer(other);
				}
				((PlayerFight) spectator).newPlayer(player);
				kick = false;
			}
		} else if (preFights.containsKey(player.getUniqueId())) {
			Bukkit.broadcastMessage("Prefight second player");
			PreFight preFight = preFights.get(player.getUniqueId());
			preFight.complet(player);
			kick = false;
		} else {
			FightData fightData = sorci.getManagerFightData().getFightDataPlayer(player.getUniqueId());
			Bukkit.broadcastMessage("Get fightdata in bbd");
			if (fightData != null && fightData.getStat() == FightStat.WAITING) {
				Bukkit.broadcastMessage("First player create prefight");
				PreFight preFight = PreFight.createPreFight(player, fightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				Bukkit.broadcastMessage("JSON: "+fightData.toDocument().toJson());
				fightData.setStat(FightStat.STARTING);
				sorci.getManagerFightData().updateFightData(fightData);
				kick = false;
			}
		}
		Bukkit.broadcastMessage("kick: "+kick);
		if (kick) {
			UUID fightUUID = sorci.getManagerFightData().whichSpectate(player.getUniqueId());
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
						true
				);
				fight.initPacketForViewFight(spectator);
				fight.addSpectator(spectator);
				spectator.initBarSpectator();
			} else if (player.hasPermission(sorci.getParameter().getString("perm_admin"))) {
				for (Player other : Bukkit.getOnlinePlayers())
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
		Bukkit.broadcastMessage("--------- end ---------");
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
								sorci.getManagerFightData().updateFightData(fightData);
							} catch (Exception e) {
								sorci.getManagerFightData().removeFightDataFight(fightData);
								Bukkit.getLogger().warning(RenderException.renderException(e));
								sendLobbyPlayer(preFight.getPlayer1());
								sendLobbyPlayer(preFight.getPlayer2());
							}
						});
						continue;
					}
				} else {
					sendLobbyPlayer(preFight.getPlayer1());
				}
				sorci.getManagerFightData().removeFightDataFight(fightData);
			}
		}
		for (Fight fight : new ArrayList<Fight>(this.fights.values()))
			try {
				fight.update();
				if (fight.isDeleted()) {
					sorci.getManagerFightData().removeFightDataFight(fight.getFightData());
					playerInstance.remove(fight.getPlayer1().getUUID());
					playerInstance.remove(fight.getPlayer2().getUUID());
					for (FightSpectator spectator : fight.getSpectators())
						playerInstance.remove(spectator.getUUID());
					fights.remove(fight.getFightData().getFightUUID());
				}
			} catch (Exception e) {
				Bukkit.getLogger().warning(RenderException.renderException(e));
			}
	}
	
	public void stop() {
		for (Fight fight : new ArrayList<Fight>(this.fights.values()))
			sorci.getManagerFightData().removeFightDataFight(fight.getFightData());
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
}
