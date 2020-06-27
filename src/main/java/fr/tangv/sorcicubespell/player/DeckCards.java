package fr.tangv.sorcicubespell.player;

import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.manager.ManagerCards;

public class DeckCards {

	public final static int size = 20;
	private Card[] cards;
	private CardFaction faction;
	
	public DeckCards(Card[] cards, CardFaction faction) throws Exception {
		if (cards.length != size)
			throw new Exception("Size deck is different of "+size+" cards !");
		this.cards = cards;
		this.faction = faction;
	}
	
	public boolean isComplet() {
		for (Card card : cards)
			if (card == null)
				return false;
		return true;
	}
	
	public Card getCard(int i) {
		return cards[i];
	}
	
	public void setCard(int i, Card card) {
		cards[i] = card;
	}
	
	public int size() {
		return size;
	}
	
	public CardFaction getFaction() {
		return faction;
	}

	public void setFaction(CardFaction faction) {
		this.faction = faction;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("faction", faction.name());
		for (int i = 0; i < size; i++)
			doc.append(Integer.toString(i), cards[i] == null ? "none" : cards[i].getUUID().toString());
		return doc;
	}
	
	public static DeckCards toDeckCards(ManagerCards manager, Document doc) throws Exception {
		Card[] cards = new Card[size];
		CardFaction faction = CardFaction.valueOf(doc.getString("faction"));
		for (int i = 0; i < size; i++) {
			String uuid = doc.getString(Integer.toString(i));
			if ("none".equals(uuid))
				cards[i] = null;
			else
				cards[i] = manager.getCart(UUID.fromString(uuid));
		}
		return new DeckCards(cards, faction);
	}
	
	public static DeckCards createDeckCarsEmpty() throws Exception {
		return createDeckCarsEmpty(CardFaction.BASIC);
	}
	
	public static DeckCards createDeckCarsEmpty(CardFaction faction) throws Exception {
		return new DeckCards(new Card[DeckCards.size], faction);
	}
	
}
