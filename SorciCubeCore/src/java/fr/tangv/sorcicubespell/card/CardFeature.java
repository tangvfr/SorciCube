package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardFeature {

	private final CardFeatureType type;
	private CardValue value;
	
	public CardFeature(CardFeatureType type, CardValue value) {
		this.type = type;
		if (value.getType() == type.getTypeValue())
			this.setValue(value);
		else
			this.setValue(CardValue.createCardValue(type.getTypeValue()));
	}

	public CardFeatureType getType() {
		return type;
	}

	public CardValue getValue() {
		return value;
	}

	public void setValue(CardValue value) {
		this.value = value;
	}

	public CardFeature clone() {
		return toCartFeature(type, toDocument());
	}
	
	public Document toDocument() {
		return value.toDocument();
	}
	
	public static CardFeature toCartFeature(CardFeatureType type, Document document) {
		return new CardFeature(type, CardValue.toCardValue(document));
	}
	
}