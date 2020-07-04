package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.EventFight;
import fr.tangv.sorcicubespell.fight.Fight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.PreFightData;

public class ManagerFight implements Runnable {

	private SorciCubeSpell sorci;
	private HashMap<UUID, PreFight> preFights;
	private Vector<Fight> fights;
	
	public ManagerFight(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.preFights = new HashMap<UUID, PreFight>();
		this.fights = new Vector<Fight>();
		//event
		Bukkit.getPluginManager().registerEvents(new EventFight(this), sorci);
		Bukkit.getScheduler().runTaskTimer(sorci, this, 0, 1);
	}
	
	public void playerJoin(Player player) {
		if (preFights.containsKey(player.getUniqueId())) {
			PreFight preFight = preFights.get(player.getUniqueId());
			preFights.remove(preFight.getPlayerUUID2());
			fights.add(new Fight(sorci, preFight, player));
			return;
		} else {
			PreFightData preFightData = sorci.getManagerPreFightData().getPreFightData(player.getUniqueId());
			if (preFightData != null) {
				sorci.getManagerPreFightData().removePreFightData(player.getUniqueId());
				PreFight preFight = PreFight.createPreFight(player, preFightData);
				preFights.put(preFight.getPlayerUUID2(), preFight);
				return;
			}
		}
		sorci.sendPlayerToServer(player, sorci.getNameServerLobby());
	}
	
	public void playerQuit(Player player) {
		for (PreFight preFight : preFights.values())
			if (preFight.getPlayerUUID1().equals(player.getUniqueId())) {
				preFights.remove(preFight.getPlayerUUID2());
				return;
			}
		for (int i = 0; i < fights.size(); i++) {
			Fight fight = fights.get(i);
			if (fight.getPlayer1().equals(player) || fight.getPlayer2().equals(player)) {
				fight.end(player);
				fights.remove(i);
				return;
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
			Fight fight = fights.get(i);
			fight.update();
			if (fight.isEnd()) {
				fight.end();
				fights.remove(i);
				i--;
			}
		}
	}
	
}
