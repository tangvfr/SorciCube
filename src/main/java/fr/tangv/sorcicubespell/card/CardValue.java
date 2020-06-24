package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardValue {

	private Object value;
	private TypeValue type;
	
	public static CardValue createCardValue(TypeValue type) {
		switch (type) {
			case TEXT:
				return new CardValue("");
			case NUMBER:
				return new CardValue(0);
			case BOOL:
				return new CardValue(false);
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

	public CardValue(boolean value) {
		this.value = value;
		this.type = CardValue.TypeValue.BOOL;
	}
	
	public boolean isString() {
		return this.type == CardValue.TypeValue.TEXT;
	}
	
	public boolean isNone() {
		return this.type == CardValue.TypeValue.NONE;
	}
	
	public boolean isInt() {
		return this.type == CardValue.TypeValue.NUMBER;
	}
	
	public boolean isBoolean() {
		return this.type == CardValue.TypeValue.BOOL;
	}
	
	public String asString() {
		return (String) value;
	}
	
	public int asInt() {
		return (int) value;
	}
	
	public Boolean asBollean() {
		return (boolean) value;
	}
	
	public TypeValue getType() {
		return type;
	}
	
	@Override
	public String toString() {
		if (type == TypeValue.NONE)
			return "none";
		return this.value.toString();
	}
	
	public Document toDocument() {
		return new Document("type", this.type.name()).append("value", this.value);
	}
	
	public static CardValue toCartValue(Document document) {
		TypeValue type = TypeValue.valueOf(document.getString("type"));
		switch (type) {
			case TEXT:
				return new CardValue(document.getString("value"));
			case NUMBER:
				return new CardValue(document.getInteger("value"));
			case BOOL:
				return new CardValue(document.getBoolean("value"));
			case NONE:
				return new CardValue();
		}
		return null;
	}
	
	public enum TypeValue {
		TEXT(),
		NUMBER(),
		BOOL(),
		NONE();
	}
	
}
