package fr.tangv.sorcicubespell.player;

import org.bson.Document;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.manager.ManagerCards;

public class PlayerFeature {

	private Player player;
	private DeckCards deck1;
	private DeckCards deck2;
	private DeckCards deck3;
	private DeckCards deck4;
	private DeckCards deck5;
	private int unlockDecks;
	
	public PlayerFeature(Player player,
			DeckCards deck1,
			DeckCards deck2,
			DeckCards deck3,
			DeckCards deck4,
			DeckCards deck5,
			int unlockDecks
		) {
		this.player = player;
		this.unlockDecks = unlockDecks;
		this.deck1 = deck1;
		this.deck2 = deck2;
		this.deck3 = deck3;
		this.deck4 = deck4;
		this.deck5 = deck5;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getUnlockDecks() {
		return unlockDecks;
	}

	public void setUnlockDecks(int unlockDecks) {
		this.unlockDecks = unlockDecks;
	}

	public DeckCards getDeck1() {
		return deck1;
	}
	
	public DeckCards getDeck2() {
		return deck2;
	}
	
	public DeckCards getDeck3() {
		return deck3;
	}
	
	public DeckCards getDeck4() {
		return deck4;
	}
	
	public DeckCards getDeck5() {
		return deck5;
	}
	
	public Document toDocument() {
		Document doc = new Document()
				.append("uuid", player.getUniqueId().toString())
				.append("deck1", deck1.toDocument())
				.append("deck2", deck2.toDocument())
				.append("deck3", deck3.toDocument())
				.append("deck4", deck4.toDocument())
				.append("deck5", deck5.toDocument())
				.append("unlock", unlockDecks)
			;
		return doc;
	}
	
	public static PlayerFeature toPlayerFeature(ManagerCards manager, Player player, Document doc) throws Exception {
		DeckCards deck1 = DeckCards.toDeckPlayer(manager, doc.get("deck1", Document.class));
		DeckCards deck2 = DeckCards.toDeckPlayer(manager, doc.get("deck2", Document.class));
		DeckCards deck3 = DeckCards.toDeckPlayer(manager, doc.get("deck3", Document.class));
		DeckCards deck4 = DeckCards.toDeckPlayer(manager, doc.get("deck4", Document.class));
		DeckCards deck5 = DeckCards.toDeckPlayer(manager, doc.get("deck5", Document.class));
		int unlockDecks = doc.getInteger("unlock");
		return new PlayerFeature(player, deck1, deck2, deck3, deck4, deck5, unlockDecks);
	}
	
	public Document toUUIDDocument() {
		return Card.toUUIDDocument(player.getUniqueId());
	}
	
}
