package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.util.Cooldown;

public class PreFight extends PreFightData {

	private Player player1;
	private Cooldown cooldown;
	
	public static PreFight createPreFight(Player player, PreFightData data) {
		if (player.getUniqueId().equals(data.getPlayerUUID1()))
			return new PreFight(player, data.getPlayerUUID2(), data.getPlayer1DeckUse(), data.getPlayer2DeckUse(), data.getFightType());
		else
			return new PreFight(player, data.getPlayerUUID1(), data.getPlayer2DeckUse(), data.getPlayer1DeckUse(), data.getFightType());
	}
	
	private PreFight(Player player1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					FightType fightType) {
		super(player1.getUniqueId(), playerUUID2, player1DeckUse, player2DeckUse, fightType);
		this.player1 = player1;
		this.cooldown = new Cooldown(10_000);
		cooldown.start();
	}
	
	public boolean updateOutOfDate() {
		return cooldown.update();
	}
	
	public Player getPlayer1() {
		return player1;
	}
	
}
