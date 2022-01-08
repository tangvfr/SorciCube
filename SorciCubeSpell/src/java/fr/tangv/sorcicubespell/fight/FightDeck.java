package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.player.DeckCards;

public class FightDeck {

	private final Vector<Card> cardsUse;
	private final ConcurrentLinkedQueue<Card> cards;
	
	public FightDeck(DeckCards deck) throws Exception {
		if (!deck.isComplet())
			throw new Exception("Deck is not complet !");
		this.cardsUse = new Vector<Card>(Arrays.asList(deck.getCards()));
		this.cards = new ConcurrentLinkedQueue<Card>();
		mixCards();
	}
	
	private synchronized void mixCards() {
		while (!cardsUse.isEmpty())
			cards.add(cardsUse.remove((int) (cardsUse.size()*Math.random())));
	}
	
	public synchronized Card pickCard() {
		if (cards.isEmpty())
			mixCards();
		Card card = cards.remove();
		cardsUse.add(card);
		return card.clone();
	}
	
}
