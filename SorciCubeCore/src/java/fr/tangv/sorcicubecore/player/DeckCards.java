package fr.tangv.sorcicubecore.player;

import java.util.Map;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardFaction;

public class DeckCards extends ListCards {

	public final static int size = 20;
	private CardFaction faction;
	
	public DeckCards(ListCards listCards, CardFaction faction) throws DeckException {
		this(listCards.getCards(), faction);
	}
	
	public DeckCards(Card[] cards, CardFaction faction) throws DeckException {
		super(cards);
		if (cards.length != size)
			throw new DeckException("Size deck is different of "+size+" cards !");
		this.faction = faction;
	}
	
	public CardFaction getFaction() {
		return faction;
	}

	public void setFaction(CardFaction faction) {
		this.faction = faction;
	}

	@Override
	public Document toDocument() {
		Document doc = super.toDocument();
		doc.append("faction", faction.name());
		return doc;
	}
	
	public static DeckCards toDeckCards(Map<UUID, Card> manager, Document doc) throws DeckException {
		ListCards listCards = ListCards.toListCards(manager, doc);
		CardFaction faction = CardFaction.valueOf(doc.getString("faction"));
		return new DeckCards(listCards, faction);
	}
	
	public static DeckCards createDeckCardsEmpty() throws DeckException {
		return createDeckCardsEmpty(CardFaction.BASIC);
	}
	
	public static DeckCards createDeckCardsEmpty(CardFaction faction) throws DeckException {
		return new DeckCards(new Card[DeckCards.size], faction);
	}
	
}
