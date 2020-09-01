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
		if (playerInstance.containsKey(player.getUniqueId())) {
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
			PreFight preFight = preFights.get(player.getUniqueId());
			preFight.complet(player);
			kick = false;
		} else {
			FightData preFightData = sorci.getManagerFightData().getFightDataPlayer(player.getUniqueId());
			if (preFightData != null) {
				sorci.getManagerFightData().changeStatFightDataFight(preFightData.getFightUUID(), FightStat.STARTING);
				PreFight preFight = PreFight.createPreFight(player, preFightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				kick = false;
			}
		}
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
				if (preFight.isComplet()) {
					if (!preFight.getPlayer1().isOnline() || !preFight.getPlayer2().isOnline()) {
						sendLobbyPlayer(preFight.getPlayer1());
						sendLobbyPlayer(preFight.getPlayer2());
					} else {
						Bukkit.getScheduler().runTask(sorci, () -> {
							try {
								Fight fight = fights.put(preFight.getFightUUID(), new Fight(sorci, preFight));
								playerInstance.put(fight.getPlayer1().getUUID(), fight.getPlayer1());
								playerInstance.put(fight.getPlayer2().getUUID(), fight.getPlayer2());
								sorci.getManagerFightData().changeStatFightDataFight(fight.getFightUUID(), FightStat.START);
							} catch (Exception e) {
								sorci.getManagerFightData().removeFightDataFight(preFight.getFightUUID());
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
				sorci.getManagerFightData().removeFightDataFight(preFight.getFightUUID());
			}
		}
		for (Fight fight : new ArrayList<Fight>(this.fights.values()))
			try {
				fight.update();
				if (fight.isDeleted()) {
					sorci.getManagerFightData().removeFightDataFight(fight.getFightUUID());
					playerInstance.remove(fight.getPlayer1().getUUID());
					playerInstance.remove(fight.getPlayer2().getUUID());
					for (FightSpectator spectator : fight.getSpectators())
						playerInstance.remove(spectator.getUUID());
					fights.remove(fight.getFightUUID());
				}
			} catch (Exception e) {
				Bukkit.getLogger().warning(RenderException.renderException(e));
			}
	}
	
	public void stop() {
		for (Fight fight : new ArrayList<Fight>(this.fights.values()))
			sorci.getManagerFightData().removeFightDataFight(fight.getFightUUID());
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
}
