package fr.tangv.sorcicubecore.handler.formater;

import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;

public class HandlerCardsFormater implements HandlerObjectsFormater<UUID, Card> {

	@Override
	public String getType() {
		return "card";
	}

	@Override
	public Document toDocument(Card value) {
		return value.toDocument();
	}

	@Override
	public UUID getKey(Card value) {
		return value.getUUID();
	}

	@Override
	public Card toValue(Document doc) {
		return Card.toCard(doc);
	}
	
}
