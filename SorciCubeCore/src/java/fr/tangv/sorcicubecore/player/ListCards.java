package fr.tangv.sorcicubecore.player;

import java.util.Map;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;

public class ListCards {

	private Card[] cards;
	
	public ListCards(Card[] cards) {
		this.cards = cards;
	}
	
	public int calcAverageCost() {
		int number = 0;
		int some = 0;
		for (int i = 0; i < cards.length; i++) {
			Card card = cards[i];
			if (card != null) {
				some += card.getMana();
				number++;
			}
		}
		return (number <= 0 || some <= 0) ? 0 : (some*10/number);
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
	
	public static ListCards toListCards(Map<UUID, Card> manager, Document doc) {
		int numberCard = doc.getInteger("size", 0);
		Card[] cards = new Card[numberCard];
		for (int i = 0; i < numberCard; i++) {
			String uuid = doc.getString(Integer.toString(i));
			if ("none".equals(uuid))
				cards[i] = null;
			else
				cards[i] = manager.get(UUID.fromString(uuid));
		}
		return new ListCards(cards);
	}
	
	public static ListCards createListCardsEmpty(int size) {
		return new ListCards(new Card[size]);
	}
	
}
