package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubespell.card.CardFaction;

public class FightData {

	private final UUID fightUUID;
	private final UUID playerUUID1;
	private final UUID playerUUID2;
	private final CardFaction factionPlayer1;
	private final CardFaction factionPlayer2;
	private final byte levelPlayer1;
	private final byte levelPlayer2;
	private final int player1DeckUse;
	private final int player2DeckUse;
	private final FightType fightType;
	private volatile FightStat stat;
	private final String server;
	
	public FightData(UUID fightUUID,
					UUID playerUUID1,
					UUID playerUUID2,
					int player1DeckUse,
					int player2DeckUse,
					CardFaction factionPlayer1,
					CardFaction factionPlayer2,
					byte levelPlayer1,
					byte levelPlayer2,
					FightType fightType,
					FightStat stat,
					String server) {
		this.fightUUID = fightUUID;
		this.playerUUID1 = playerUUID1;
		this.playerUUID2 = playerUUID2;
		this.player1DeckUse = player1DeckUse;
		this.player2DeckUse = player2DeckUse;
		this.factionPlayer1 = factionPlayer1;
		this.factionPlayer2 = factionPlayer2;
		this.levelPlayer1 = levelPlayer1;
		this.levelPlayer2 = levelPlayer2;
		this.fightType = fightType;
		this.stat = stat;
		this.server = server;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setStat(FightStat stat) {
		this.stat = stat;
	}
	
	public FightStat getStat() {
		return stat;
	}
	
	public UUID getFightUUID() {
		return fightUUID;
	}
	
	public UUID getPlayerUUID1() {
		return playerUUID1;
	}
	
	public UUID getPlayerUUID2() {
		return playerUUID2;
	}
	
	public CardFaction getFactionDeckPlayer1() {
		return factionPlayer1;
	}
	
	public CardFaction getFactionDeckPlayer2() {
		return factionPlayer2;
	}
	
	public byte getLevelPlayer1() {
		return levelPlayer1;
	}
	
	public byte getLevelPlayer2() {
		return levelPlayer2;
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
		return new Document("fight_uuid", fightUUID.toString())
				.append("fight_type", fightType.name())
				.append("player1", playerUUID1.toString())
				.append("player2", playerUUID2.toString())
				.append("deck_player1", player1DeckUse)
				.append("deck_player2", player2DeckUse)
				.append("faction_player1", factionPlayer1.name())
				.append("faction_player2", factionPlayer2.name())
				.append("level_player1", levelPlayer1)
				.append("level_player2", levelPlayer2)
				.append("stat", stat.toString())
				.append("server", server);
	}
	
	public static FightData toFightData(Document doc) {
		return new FightData(UUID.fromString(doc.getString("fight_uuid")),
				UUID.fromString(doc.getString("player1")),
				UUID.fromString(doc.getString("player2")),
				doc.getInteger("deck_player1"),
				doc.getInteger("deck_player2"),
				CardFaction.valueOf(doc.getString("faction_player1")),
				CardFaction.valueOf(doc.getString("faction_player2")),
				doc.getInteger("level_player1").byteValue(),
				doc.getInteger("level_player2").byteValue(),
				FightType.valueOf(doc.getString("fight_type")),
				FightStat.valueOf(doc.getString("stat")),
				doc.getString("server"));
	}
	
}
