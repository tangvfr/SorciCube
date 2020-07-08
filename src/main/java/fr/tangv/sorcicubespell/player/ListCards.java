package fr.tangv.sorcicubespell.player;

import java.util.HashMap;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubespell.card.Card;

public class ListCards {

	private Card[] cards;
	
	public ListCards(Card[] cards) {
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
		return cards.length;
	}
	
	public Card[] getCards() {
		return cards;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("size", size());
		for (int i = 0; i < size(); i++)
			doc.append(Integer.toString(i), cards[i] == null ? "none" : cards[i].getUUID().toString());
		return doc;
	}
	
	public static ListCards toListCards(HashMap<UUID, Card> hashCards, Document doc) {
		int numberCard = doc.getInteger("size", 0);
		Card[] cards = new Card[numberCard];
		for (int i = 0; i < numberCard; i++) {
			String uuid = doc.getString(Integer.toString(i));
			if ("none".equals(uuid))
				cards[i] = null;
			else {
				UUID id = UUID.fromString(uuid);
				cards[i] = hashCards.containsKey(id) ? hashCards.get(id) : null;
			}
		}
		return new ListCards(cards);
	}
	
	public static ListCards createListCardsEmpty(int size) {
		return new ListCards(new Card[size]);
	}
	
}
