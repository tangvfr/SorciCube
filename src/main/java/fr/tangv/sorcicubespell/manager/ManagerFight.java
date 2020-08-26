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
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.PreFightData;
import fr.tangv.sorcicubespell.fight.ValueFight;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerFight implements Runnable {
	
	private final SorciCubeSpell sorci;
	private final ConcurrentHashMap<UUID, PreFight> preFights;
	private final Vector<Fight> fights;
	private final Vector<FightArena> arena;
	private final ConcurrentHashMap<UUID, FightSpectator> playerInstance;
	
	public ManagerFight(SorciCubeSpell sorci) throws Exception {
		this.sorci = sorci;
		ValueFight.V = new ValueFight(sorci);
		this.playerInstance = new ConcurrentHashMap<UUID, FightSpectator>();
		this.preFights = new ConcurrentHashMap<UUID, PreFight>();
		this.fights = new Vector<Fight>();
		this.arena = new Vector<FightArena>();
		for (String name : sorci.getArenaConfig().getKeys(false))
			this.arena.add(new FightArena(name, sorci.getArenaConfig().getConfigurationSection(name)));
		if (arena.size() <= 0)
			throw new Exception("Nothing arena !");
		//event
		Bukkit.getPluginManager().registerEvents(new EventFight(this), sorci);
		Bukkit.getScheduler().runTaskTimerAsynchronously(sorci, this, 0, 5);
	}
	
	public void putFightSpectator(FightSpectator spectator) {
		playerInstance.put(spectator.getUUID(), spectator);
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
	
	public void playerJoin(Player player) {
		boolean kick = true;
		if (preFights.containsKey(player.getUniqueId())) {
			PreFight preFight = preFights.get(player.getUniqueId());
			preFight.complet(player);
			kick = false;
		} else {
			PreFightData preFightData = sorci.getManagerPreFightData().getPreFightData(player.getUniqueId());
			if (preFightData != null) {
				sorci.getManagerPreFightData().removePreFightData(player.getUniqueId());
				PreFight preFight = PreFight.createPreFight(player, preFightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				kick = false;
			}
		}
		if (kick) {
			if (player.hasPermission(sorci.getParameter().getString("perm_admin"))) {
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
	
	public void playerQuit(Player player) {
		if (playerInstance.containsKey(player.getUniqueId())) {
			FightSpectator spectator = playerInstance.get(player.getUniqueId());
			if (spectator.isFightPlayer()) {
				PlayerFight playerFight = (PlayerFight) spectator;
				Fight fight = playerFight.getFight();
				if (fight.getPlayer1().isOnline() || fight.getPlayer2().isOnline()) {
					if (!fight.isEnd())
						fight.end(playerFight);
				} else
					fights.remove(fight);
			}
			playerInstance.remove(player.getUniqueId());
			return;
		}
		for (PreFight preFight : preFights.values())
			if (preFight.getPlayerUUID1().equals(player.getUniqueId())) {
				preFights.remove(preFight.getPlayerUUID2());
				return;
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
								fights.add(new Fight(sorci, preFight));
							} catch (Exception e) {
								Bukkit.getLogger().warning(RenderException.renderException(e));
								sendLobbyPlayer(preFight.getPlayer1());
								sendLobbyPlayer(preFight.getPlayer2());
							}
						});
					}
				} else {
					sendLobbyPlayer(preFight.getPlayer1());
				}
			}
		}
		for (int i = 0; i < fights.size(); i++) {
			try {
				Fight fight = fights.get(i);
				fight.update();
				if (fight.isDeleted()) {
					fights.remove(i);
					i--;
				}
			} catch (Exception e) {
				Bukkit.getLogger().warning(RenderException.renderException(e));
			}
		}
	}
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
}
