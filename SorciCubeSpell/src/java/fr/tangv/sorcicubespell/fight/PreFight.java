package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.util.Cooldown;
import fr.tangv.sorcicubespell.player.DataPlayer;

public class PreFight {

	private final FightData fightData;
	private final Player player1;
	private final PlayerFeatures features1;
	private final UUID playerUUID2;
	private final int player1DeckUse;
	private final int player2DeckUse;
	private final DataPlayer dataPlayer1;
	private Cooldown cooldown;
	private volatile Player player2;
	private volatile PlayerFeatures features2;
	private volatile DataPlayer dataPlayer2;
	
	public static PreFight createPreFight(Player player, DataPlayer dataPlayer, PlayerFeatures features, FightData data) {
		if (player.getUniqueId().equals(data.getPlayerUUID1()))
			return new PreFight(data, player, dataPlayer, features, data.getPlayerUUID2(), data.getPlayer1DeckUse(), data.getPlayer2DeckUse(), data.getFightType());
		else
			return new PreFight(data, player, dataPlayer, features, data.getPlayerUUID1(), data.getPlayer2DeckUse(), data.getPlayer1DeckUse(), data.getFightType());
	}
	
	private PreFight(FightData fightData,
					Player player1,
					DataPlayer dataPlayer1,
					PlayerFeatures features1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					FightType fightType) {
		this.fightData = fightData;
		this.player1 = player1;
		this.features1 = features1;
		this.playerUUID2 = playerUUID2;
		this.player1DeckUse = player1DeckUse;
		this.player2DeckUse = player2DeckUse;
		this.dataPlayer1 = dataPlayer1;
		this.player2 = null;
		this.cooldown = new Cooldown(3_000);
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
	
	public DataPlayer getDataPlayer1() {
		return dataPlayer1;
	}

	public DataPlayer getDataPlayer2() {
		return dataPlayer2;
	}

	public void complet(Player player2, DataPlayer dataPlayer2, PlayerFeatures features2) {
		this.player2 = player2;
		this.features2 = features2;
		this.dataPlayer2 = dataPlayer2;
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
