package fr.tangv.sorcicubespell.player;

import java.util.List;

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
	private List<String> cardsUnlocks;
	
	public PlayerFeature(Player player,
			DeckCards deck1,
			DeckCards deck2,
			DeckCards deck3,
			DeckCards deck4,
			DeckCards deck5,
			int unlockDecks,
			List<String> cardsUnlocks
		) {
		this.player = player;
		this.unlockDecks = unlockDecks;
		this.deck1 = deck1;
		this.deck2 = deck2;
		this.deck3 = deck3;
		this.deck4 = deck4;
		this.deck5 = deck5;
		this.cardsUnlocks = cardsUnlocks;
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
	
	public DeckCards getDeck(int number) {
		switch (number) {
			case 1:
				return deck1;
	
			case 2:
				return deck2;
				
			case 3:
				return deck3;
				
			case 4:
				return deck4;
				
			case 5:
				return deck5;
				
			default:
				return null;
		}
	}
	
	public List<String> getCardsUnlocks() {
		return cardsUnlocks;
	}
	
	public Document toDocument() {
		Document doc = new Document()
				.append("uuid", player.getUniqueId().toString())
				.append("deck1", deck1.toDocument())
				.append("deck2", deck2.toDocument())
				.append("deck3", deck3.toDocument())
				.append("deck4", deck4.toDocument())
				.append("deck5", deck5.toDocument())
				.append("deck_unlock", unlockDecks)
				.append("cards_unlocks", cardsUnlocks)
			;
		return doc;
	}
	
	public static PlayerFeature toPlayerFeature(Player player, ManagerCards manager, Document doc) throws Exception {
		DeckCards deck1 = DeckCards.toDeckCards(manager, doc.get("deck1", Document.class));
		DeckCards deck2 = DeckCards.toDeckCards(manager, doc.get("deck2", Document.class));
		DeckCards deck3 = DeckCards.toDeckCards(manager, doc.get("deck3", Document.class));
		DeckCards deck4 = DeckCards.toDeckCards(manager, doc.get("deck4", Document.class));
		DeckCards deck5 = DeckCards.toDeckCards(manager, doc.get("deck5", Document.class));
		int unlockDecks = doc.getInteger("deck_unlock");
		List<String> cardsUnlocks = doc.getList("cards_unlocks", String.class);
		return new PlayerFeature(player, deck1, deck2, deck3, deck4, deck5, unlockDecks, cardsUnlocks);
	}
	
	public Document toUUIDDocument() {
		return Card.toUUIDDocument(player.getUniqueId());
	}
	
}
