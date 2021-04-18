package fr.tangv.sorcicubespell.manager;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubecore.fight.FightStat;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.prefight.EventDuelCreator;

public class ManagerCreatorFight {

	private final SorciCubeSpell sorci;
	private final Location locNoClassified;
	private final Location locDuel;
	private final Location locNPC;
	private final Vector<Player> duelPlayers;
	private volatile Player noClassified;
	
	public ManagerCreatorFight(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.locNoClassified = SorciCubeSpell.convertLocation(sorci.config().locations.locationNoclassified);
		this.locDuel = SorciCubeSpell.convertLocation(sorci.config().locations.locationDuel);
		this.locNPC = SorciCubeSpell.convertLocation(sorci.config().locations.locationNpc);
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
		try {
			sorci.getHandlerFightData().addFightData(
					new FightData(
							UUID.randomUUID(),
							player1.getUUID(),
							player2.getUUID(),
							player1.getDeckEdit(),
							player2.getDeckEdit(),
							player1.getPlayerFeatures().getDeck(player1.getDeckEdit()).getFaction(),
							player2.getPlayerFeatures().getDeck(player2.getDeckEdit()).getFaction(),
							player1.getPlayerFeatures().getLevel(),
							player2.getPlayerFeatures().getLevel(),
							player1.getDisplayGroup(),
							player2.getDisplayGroup(),
							type,
							FightStat.WAITING,
							server
						)
				);
			sorci.sendPlayerToServer(player1.getPlayer(), server);
			sorci.sendPlayerToServer(player2.getPlayer(), server);
		} catch (IOException | ResponseRequestException | RequestException e) {
			e.printStackTrace();
			player1.getPlayer().sendMessage(e.getMessage());
			player2.getPlayer().sendMessage(e.getMessage());
		}
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
