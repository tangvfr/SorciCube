package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.player.DeckCards;

public class FightDeck {

	private Vector<Card> cardsUse;
	private ConcurrentLinkedQueue<Card> cards;
	
	public FightDeck(DeckCards deck) throws Exception {
		if (!deck.isComplet())
			throw new Exception("Deck is not complet !");
		this.cardsUse = new Vector<Card>(Arrays.asList(deck.getCards()));
		this.cards = new ConcurrentLinkedQueue<Card>();
		mixCards();
	}
	
	private void mixCards() {
		while (cardsUse.size() > 0) {
			Card card = cardsUse.elementAt((int) (cards.size()*Math.random()));
			cardsUse.remove(card);
			cards.add(card);
		}
	}
	
	public Card pickCard() {
		if (cards.size() <= 0)
			mixCards();
		return cards.remove();
	}
	
}
