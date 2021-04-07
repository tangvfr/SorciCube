package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.util.Cooldown;

public class PreFight {

	private final FightData fightData;
	private final Player player1;
	private final PlayerFeatures features1;
	private final UUID playerUUID2;
	private final int player1DeckUse;
	private final int player2DeckUse;
	private final byte levelPlayer1;
	private final byte levelPlayer2;
	private final String groupPlayer1;
	private final String groupPlayer2;
	private Cooldown cooldown;
	private volatile Player player2;
	private volatile PlayerFeatures features2;
	
	public static PreFight createPreFight(Player player, PlayerFeatures features, FightData data) {
		if (player.getUniqueId().equals(data.getPlayerUUID1()))
			return new PreFight(data, player, features, data.getPlayerUUID2(), data.getPlayer1DeckUse(), data.getPlayer2DeckUse(),
					data.getLevelPlayer1(), data.getLevelPlayer2(), data.getGroupPlayer1(), data.getGroupPlayer2(), data.getFightType());
		else
			return new PreFight(data, player, features, data.getPlayerUUID1(), data.getPlayer2DeckUse(), data.getPlayer1DeckUse(),
					data.getLevelPlayer2(), data.getLevelPlayer1(), data.getGroupPlayer2(), data.getGroupPlayer1(), data.getFightType());
	}
	
	private PreFight(FightData fightData,
					Player player1,
					PlayerFeatures features1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					byte levelPlayer1,
					byte levelPlayer2,
					String groupPlayer1,
					String groupPlayer2,
					FightType fightType) {
		this.fightData = fightData;
		this.player1 = player1;
		this.features1 = features1;
		this.playerUUID2 = playerUUID2;
		this.player1DeckUse = player1DeckUse;
		this.player2DeckUse = player2DeckUse;
		this.levelPlayer1 = levelPlayer1;
		this.levelPlayer2 = levelPlayer2;
		this.groupPlayer1 = groupPlayer1;
		this.groupPlayer2 = groupPlayer2;
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
	
	public PlayerFeatures getFeatures1() {
		return features1;
	}

	public PlayerFeatures getFeatures2() {
		return features2;
	}

	public int getPlayer1DeckUse() {
		return player1DeckUse;
	}
	
	public int getPlayer2DeckUse() {
		return player2DeckUse;
	}
	
	public byte getLevelPlayer1() {
		return levelPlayer1;
	}
	
	public byte getLevelPlayer2() {
		return levelPlayer2;
	}
	
	public String getGroupPlayer1() {
		return groupPlayer1;
	}

	public String getGroupPlayer2() {
		return groupPlayer2;
	}
	
	public void complet(Player player2, PlayerFeatures features2) {
		this.player2 = player2;
		this.features2 = features2;
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
