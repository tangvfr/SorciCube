package fr.tangv.sorcicubespell.card;

import java.util.UUID;

import org.bson.Document;

public class CardValue {

	private static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	private final Object value;
	private final TypeValue type;
	
	public static CardValue createCardValue(TypeValue type) {
		switch (type) {
			case UUID:
				return new CardValue(EMPTY_UUID);
			case NUMBER:
				return new CardValue(0);
			case ROUND:
				return new CardValue(0, false);
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
	
	public CardValue(UUID value) {
		this.value = value;
		this.type = CardValue.TypeValue.UUID;
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
		this.value = path ? value*2 : value;
		this.type = CardValue.TypeValue.ROUND;
	}
	
	public CardValue(CardSkin value) {
		this.value = value;
		this.type = CardValue.TypeValue.SKIN;
	}
	
	public boolean isUUID() {
		return this.type == CardValue.TypeValue.UUID;
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
	
	public boolean isSkin() {
		return this.type == CardValue.TypeValue.SKIN;
	}
	
	public UUID asUUID() {
		return (UUID) value;
	}
	
	public int asNumber() {
		return (int) value;
	}
	
	public int asRound() {
		return (int) value;
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
		else
			return this.value.toString();
	}
	
	public Document toDocument() {
		if (type == TypeValue.SKIN)
			return new Document("type", this.type.name()).append("value", ((CardSkin) this.value).toDocument());
		else if (type == TypeValue.UUID)
			return new Document("type", this.type.name()).append("value", this.value.toString());
		else
			return new Document("type", this.type.name()).append("value", this.value);
	}
	
	public static CardValue toCardValue(Document document) {
		switch (TypeValue.valueOf(document.getString("type"))) {
			case UUID:
				return new CardValue(UUID.fromString(document.getString("value")));
			case NUMBER:
				return new CardValue(document.getInteger("value"));
			case ROUND:
				return new CardValue(document.getInteger("value"), false);
			case SKIN:
				return new CardValue(CardSkin.toCartSkin(document.get("value", Document.class)));
			case NONE:
				return new CardValue();
		}
		return null;
	}
	
	public enum TypeValue {
		UUID(),
		NUMBER(),
		ROUND(),
		SKIN(),
		NONE();
	}
	
}
