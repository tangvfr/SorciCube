package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.util.Cooldown;

public class PreFight {

	private final FightData fightData;
	private final Player player1;
	private final UUID playerUUID2;
	private final int player1DeckUse;
	private final int player2DeckUse;
	private Cooldown cooldown;
	private volatile Player player2;
	
	public static PreFight createPreFight(Player player, FightData data) {
		if (player.getUniqueId().equals(data.getPlayerUUID1()))
			return new PreFight(data, player, data.getPlayerUUID2(), data.getPlayer1DeckUse(), data.getPlayer2DeckUse(), data.getFightType());
		else
			return new PreFight(data, player, data.getPlayerUUID1(), data.getPlayer2DeckUse(), data.getPlayer1DeckUse(), data.getFightType());
	}
	
	private PreFight(FightData fightData,
					Player player1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					FightType fightType) {
		this.fightData = fightData;
		this.player1 = player1;
		this.playerUUID2 = playerUUID2;
		this.player1DeckUse = player1DeckUse;
		this.player2DeckUse = player2DeckUse;
		this.player2 = null;
		this.cooldown = new Cooldown(1_000);
		cooldown.start();
	}
	
	public FightData getFightData() {
		return fightData;
	}
	
	public UUID getPlayerUUID1() {
		return player1.getUniqueId();
	}
	
	public UUID getPlayerUUID2() {
		return playerUUID2;
	}
	
	public int getPlayer1DeckUse() {
		return player1DeckUse;
	}
	
	public int getPlayer2DeckUse() {
		return player2DeckUse;
	}
	
	public void complet(Player player2) {
		this.player2 = player2;
		this.cooldown = new Cooldown(1_000);
		cooldown.start();
	}
	
	public boolean isComplet() {
		return player2 != null;
	}
	
	public boolean updateOutOfDate() {
		return cooldown.update();
	}
	
	public Player getPlayer1() {
		return player1;
	}
	
	public Player getPlayer2() {
		return player2;
	}
	
}
