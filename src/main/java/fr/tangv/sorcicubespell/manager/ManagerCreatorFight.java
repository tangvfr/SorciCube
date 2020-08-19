package fr.tangv.sorcicubespell.manager;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.prefight.EventDuelCreator;

public class ManagerCreatorFight {

	private SorciCubeSpell sorci;
	private Location locNoClassified;
	private Location locDuel;
	private Location locNPC;
	private volatile Vector<Player> duelPlayers;
	private volatile Player noClassified; 
	
	public ManagerCreatorFight(SorciCubeSpell sorci) {
		this.locNoClassified = (Location) sorci.getParameter().get("location_noclassified");
		this.locDuel = (Location) sorci.getParameter().get("location_duel");
		this.locNPC = (Location) sorci.getParameter().get("location_npc");
		this.duelPlayers = new Vector<Player>();
		this.noClassified = null;
		Bukkit.getPluginManager().registerEvents(new EventDuelCreator(sorci), sorci);
	}

	public void playerLeave(Player player, boolean disconnect) {
		if (player.equals(noClassified)) {
			noClassified = null;
		} else if (duelPlayers.contains(player)) {
			duelPlayers.remove(player);
			sorci.getManagerGui().getPlayerGui(player).setInviteDuel(null);
		}
		if (!disconnect)
			player.teleport(locNPC);
	}
	
	public boolean isInDuel(Player player) {
		return duelPlayers.contains(player);
	}
	
	public boolean addInDuel(Player player) {
		return duelPlayers.add(player);
	}
	
	public Player getNoClassified() {
		return noClassified;
	}

	public void setNoClassified(Player noClassified) {
		this.noClassified = noClassified;
	}
	
	public Location getLocationFor(Player player) {
		if (player.equals(noClassified))
			return locNoClassified;
		if (duelPlayers.contains(player))
			return locDuel;
		return null;
	}
	
}
