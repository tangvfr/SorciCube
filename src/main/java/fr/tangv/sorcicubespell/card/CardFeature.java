package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardFeature {

	private CardFeatureType type;
	private CardValue value;
	
	public CardFeature(CardFeatureType type, CardValue value) {
		this.type = type;
		if (value.getType() == type.getTypeValue())
			this.setValue(value);
		else if (type == CardFeatureType.SKIN && value.isString())
			this.setValue(new CardValue(new CardSkin(value.asString())));
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
		return new CardFeature(type, CardValue.toCartValue(document));
	}
	
}