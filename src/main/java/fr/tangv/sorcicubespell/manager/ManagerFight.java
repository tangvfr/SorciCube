package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.EventFight;
import fr.tangv.sorcicubespell.fight.Fight;
import fr.tangv.sorcicubespell.fight.FightArena;
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.PreFightData;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerFight implements Runnable {
	
	private SorciCubeSpell sorci;
	private HashMap<UUID, PreFight> preFights;
	private Vector<Fight> fights;
	private Vector<FightArena> arena;
	private ConcurrentHashMap<Player, PlayerFight> playerFights;
	
	public ManagerFight(SorciCubeSpell sorci) throws Exception {
		this.sorci = sorci;
		this.playerFights = new ConcurrentHashMap<Player, PlayerFight>();
		this.preFights = new HashMap<UUID, PreFight>();
		this.fights = new Vector<Fight>();
		this.arena = new Vector<FightArena>();
		for (String name : sorci.gertArenaConfig().getKeys(false))
			this.arena.add(new FightArena(name, sorci.gertArenaConfig().getConfigurationSection(name)));
		if (arena.size() <= 0)
			throw new Exception("Nothing arena !");
		//event
		Bukkit.getPluginManager().registerEvents(new EventFight(this), sorci);
		Bukkit.getScheduler().runTaskTimerAsynchronously(sorci, this, 0, 5);
		for (Player player : Bukkit.getOnlinePlayers())
			sorci.sendPlayerToServer(player, sorci.getNameServerLobby());
	}
	
	public FightArena pickArena() {
		return arena.elementAt((int) (arena.size()*Math.random()));
	}
	
	public HashMap<UUID, PreFight> getPreFights() {
		return preFights;
	}
	
	public ConcurrentHashMap<Player, PlayerFight> getPlayerFights() {
		return playerFights;
	}
	
	public void playerJoin(Player player) {
		if (preFights.containsKey(player.getUniqueId())) {
			PreFight preFight = preFights.get(player.getUniqueId());
			preFights.remove(preFight.getPlayerUUID2());
			try {
				fights.add(new Fight(sorci, preFight, player));
				return;
			} catch (Exception e) {
				Bukkit.getLogger().warning(RenderException.renderException(e));
				sorci.sendPlayerToServer(preFight.getPlayer1(), sorci.getNameServerLobby());
				sorci.sendPlayerToServer(player, sorci.getNameServerLobby());
				return;
			}
		} else {
			PreFightData preFightData = sorci.getManagerPreFightData().getPreFightData(player.getUniqueId());
			if (preFightData != null) {
				sorci.getManagerPreFightData().removePreFightData(player.getUniqueId());
				PreFight preFight = PreFight.createPreFight(player, preFightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				return;
			}
		}
		if (!player.hasPermission(sorci.getParameter().getString("perm_admin")))
			sorci.sendPlayerToServer(player, sorci.getNameServerLobby());
	}
	
	public void playerQuit(Player player) {
		if (playerFights.containsKey(player))
			playerFights.remove(player);
		for (PreFight preFight : preFights.values())
			if (preFight.getPlayerUUID1().equals(player.getUniqueId())) {
				preFights.remove(preFight.getPlayerUUID2());
				return;
			}
		for (int i = 0; i < fights.size(); i++) {
			try {
				Fight fight = fights.get(i);
				if (fight.getPlayer1().isPlayer(player) || fight.getPlayer2().isPlayer(player)) {
					fight.end(player);
					fights.remove(i);
					return;
				}
			} catch (Exception e) {
				Bukkit.getLogger().warning(RenderException.renderException(e));
			}
		}
	}

	@Override
	public void run() {
		for (PreFight preFight : preFights.values())
			if (preFight.updateOutOfDate()) {
				preFights.remove(preFight.getPlayerUUID2());
				sorci.sendPlayerToServer(preFight.getPlayer1(), sorci.getNameServerLobby());
			}
		for (int i = 0; i < fights.size(); i++) {
			try {
				Fight fight = fights.get(i);
				fight.update();
				if (fight.isEnd()) {
					fight.end();
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
