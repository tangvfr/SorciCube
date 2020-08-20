package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardValue {

	private final Object value;
	private final TypeValue type;
	
	public static CardValue createCardValue(TypeValue type) {
		switch (type) {
			case TEXT:
				return new CardValue("");
			case NUMBER:
				return new CardValue(0);
			case ROUND:
				return new CardValue(0, false);
			case BOOL:
				return new CardValue(false);
			case SKIN:
				try {
					return new CardValue(CardSkin.createCardSkin(644618518));
				} catch (Exception e) {
					return new CardValue(); //it is not possible, except problem network
				}
			case NONE:
				return new CardValue();
		}
		return new CardValue();
	}
	
	public CardValue(String value) {
		this.value = value;
		this.type = CardValue.TypeValue.TEXT;
	}
	
	public CardValue() {
		this.value = null;
		this.type = CardValue.TypeValue.NONE;
	}
	
	public CardValue(int value) {
		this.value = value;
		this.type = CardValue.TypeValue.NUMBER;
	}
	
	public CardValue(int value, boolean path) {
		this.value = path ? value : value*2;
		this.type = CardValue.TypeValue.ROUND;
	}

	public CardValue(boolean value) {
		this.value = value;
		this.type = CardValue.TypeValue.BOOL;
	}
	
	public CardValue(CardSkin value) {
		this.value = value;
		this.type = CardValue.TypeValue.SKIN;
	}
	
	public boolean isString() {
		return this.type == CardValue.TypeValue.TEXT;
	}
	
	public boolean isNone() {
		return this.type == CardValue.TypeValue.NONE;
	}
	
	public boolean isNumber() {
		return this.type == CardValue.TypeValue.NUMBER;
	}
	
	public boolean isRound() {
		return this.type == CardValue.TypeValue.ROUND;
	}
	
	public boolean isBoolean() {
		return this.type == CardValue.TypeValue.BOOL;
	}
	
	public boolean isSkin() {
		return this.type == CardValue.TypeValue.SKIN;
	}
	
	public String asString() {
		return (String) value;
	}
	
	public int asNumber() {
		return (int) value;
	}
	
	public int asRound() {
		return (int) value;
	}
	
	public boolean asBollean() {
		return (boolean) value;
	}
	
	public CardSkin asSkin() {
		return (CardSkin) value;
	}
	
	public TypeValue getType() {
		return type;
	}
	
	@Override
	public String toString() {
		if (isNone())
			return "none";
		else if (isRound())
			return Integer.toString(asRound()/2);
		return this.value.toString();
	}
	
	public Document toDocument() {
		if (type == TypeValue.SKIN)
			return new Document("type", this.type.name()).append("value", ((CardSkin) this.value).toDocument());
		else
			return new Document("type", this.type.name()).append("value", this.value);
	}
	
	public static CardValue toCardValue(Document document) {
		TypeValue type = TypeValue.valueOf(document.getString("type"));
		switch (type) {
			case TEXT:
				return new CardValue(document.getString("value"));
			case NUMBER:
				return new CardValue(document.getInteger("value"));
			case ROUND:
				return new CardValue(document.getInteger("value"), false);
			case BOOL:
				return new CardValue(document.getBoolean("value"));
			case SKIN:
				return new CardValue(CardSkin.toCartSkin(document.get("value", Document.class)));
			case NONE:
				return new CardValue();
		}
		return null;
	}
	
	public enum TypeValue {
		TEXT(),
		NUMBER(),
		ROUND(),
		BOOL(),
		SKIN(),
		NONE();
	}
	
}
