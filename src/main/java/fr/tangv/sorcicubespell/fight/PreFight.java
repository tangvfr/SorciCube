package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.util.Cooldown;

public class PreFight extends FightData {

	private final Player player1;
	private volatile Player player2;
	private volatile Cooldown cooldown;
	
	public static PreFight createPreFight(Player player, FightData data) {
		if (player.getUniqueId().equals(data.getPlayerUUID1()))
			return new PreFight(data.getFightUUID(), player, data.getPlayerUUID2(), data.getPlayer1DeckUse(), data.getPlayer2DeckUse(), data.getFightType(), data.getServer());
		else
			return new PreFight(data.getFightUUID(), player, data.getPlayerUUID1(), data.getPlayer2DeckUse(), data.getPlayer1DeckUse(), data.getFightType(), data.getServer());
	}
	
	private PreFight(UUID fightUUID,
					Player player1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					FightType fightType,
					String server) {
		super(fightUUID, player1.getUniqueId(), playerUUID2, player1DeckUse, player2DeckUse, fightType, FightStat.STARTING, server);
		this.player1 = player1;
		this.player2 = null;
		this.cooldown = new Cooldown(1_000);
		cooldown.start();
	}
	
	public void complet(Player player2) {
		this.player2 = player2;
		this.cooldown = new Cooldown(500);
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
