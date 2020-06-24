package fr.tangv.sorcicubespell.player;

import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.manager.ManagerCards;

public class DeckCards {

	private final static int size = 20;
	private Card[] cards;
	
	public DeckCards(Card[] cards) throws Exception {
		if (cards.length != size)
			throw new Exception("Size deck is different of "+size+" cards !");
		this.cards = cards;
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
	
	public Document toDocument() {
		Document doc = new Document();
		for (int i = 0; i < size; i++)
			doc.put(Integer.toString(i), cards[i].toDocument());
		return doc;
	}
	
	public static DeckCards toDeckPlayer(ManagerCards manager, Document doc) throws Exception {
		Card[] cards = new Card[size];
		for (int i = 0; i < size; i++)
			cards[i] = manager.getCart(UUID.fromString(doc.getString(Integer.toString(i))));
		return new DeckCards(cards);
	}
	
}
