package fr.tangv.sorcicubespell.manager;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.fight.FightType;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.prefight.EventDuelCreator;

public class ManagerCreatorFight {

	private SorciCubeSpell sorci;
	private Location locNoClassified;
	private Location locDuel;
	private Location locNPC;
	private volatile Vector<Player> duelPlayers;
	private volatile Player noClassified;
	
	public ManagerCreatorFight(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locNoClassified = (Location) sorci.getParameter().get("location_noclassified");
		this.locDuel = (Location) sorci.getParameter().get("location_duel");
		this.locNPC = (Location) sorci.getParameter().get("location_npc");
		this.duelPlayers = new Vector<Player>();
		this.noClassified = null;
		Bukkit.getPluginManager().registerEvents(new EventDuelCreator(sorci, this), sorci);
	}

	public void playerLeave(Player player, boolean disconnect) {
		if (player.equals(noClassified)) {
			noClassified = null;
		} else if (duelPlayers.contains(player)) {
			duelPlayers.remove(player);
		}
		if (!disconnect)
			player.teleport(locNPC);
	}
	
	public void duelFightPlayer(PlayerGui player1, PlayerGui player2, String server) {
		duelPlayers.remove(player1.getPlayer());
		duelPlayers.remove(player2.getPlayer());
		startFightPlayer(player1, player2, server, FightType.DUEL);
	}
	
	public void startFightPlayer(PlayerGui player1, PlayerGui player2, String server, FightType type) {
		sorci.getManagerFightData().addFightData(
				new FightData(
						UUID.randomUUID(),
						player1.getPlayer().getUniqueId(),
						player2.getPlayer().getUniqueId(),
						player1.getDeckEdit(),
						player2.getDeckEdit(),
						player1.getPlayerFeature().getDeck(player1.getDeckEdit()).getFaction(),
						player2.getPlayerFeature().getDeck(player2.getDeckEdit()).getFaction(),
						player1.getPlayerFeature().getLevel(),
						player2.getPlayerFeature().getLevel(),
						type,
						FightStat.WAITING,
						server
					)
			);
		sorci.sendPlayerToServer(player1.getPlayer(), server);
		sorci.sendPlayerToServer(player2.getPlayer(), server);
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
