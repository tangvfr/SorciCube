package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.EventFight;
import fr.tangv.sorcicubespell.fight.PreFight;
import fr.tangv.sorcicubespell.fight.PreFightData;

public class ManagerFight implements Listener {

	private SorciCubeSpell sorci;
	private HashMap<UUID, PreFight> preFights;
	
	public ManagerFight(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.preFights = new HashMap<UUID, PreFight>();
		//event
		Bukkit.getPluginManager().registerEvents(new EventFight(this), sorci);
	}
	
	public void playerJoin(Player player) {
		if (preFights.containsKey(player.getUniqueId())) {
			//start
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
		//return player to lobby
	}
	
}
