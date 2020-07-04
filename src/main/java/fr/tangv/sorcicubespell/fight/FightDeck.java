package fr.tangv.sorcicubespell.fight;

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
		
	}
	
}
