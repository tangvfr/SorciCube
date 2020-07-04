package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bson.Document;

public class PreFightData {

	private UUID playerUUID1;
	private UUID playerUUID2;
	private int player1DeckUse;
	private int player2DeckUse;
	private FightType fightType;
	
	public PreFightData(UUID playerUUID1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					FightType fightType) {
		this.playerUUID1 = playerUUID1;
		this.playerUUID2 = playerUUID2;
		this.player1DeckUse = player1DeckUse;
		this.player2DeckUse = player2DeckUse;
		this.fightType = fightType;
	}
	
	public UUID getPlayerUUID1() {
		return playerUUID1;
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
	
	public FightType getFightType() {
		return fightType;
	}
	
	public Document toDocument() {
		return new Document("fight_type", fightType.name())
				.append("player1", playerUUID1.toString())
				.append("player2", playerUUID2.toString())
				.append("deck_player1", player1DeckUse)
				.append("deck_player2", player2DeckUse);
	}
	
	public static PreFightData toPreFightData(Document doc) {
		return new PreFightData(UUID.fromString(doc.getString("player1")),
				UUID.fromString(doc.getString("player2")),
				doc.getInteger("deck_player1"),
				doc.getInteger("deck_player2"),
				FightType.valueOf(doc.getString("fight_type")));
	}
	
}
