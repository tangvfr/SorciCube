package fr.tangv.sorcicubespell.carts;

import org.bson.Document;

public class CartValue {

	private Object value;
	private TypeValue type;
	
	public CartValue(String value) {
		this.value = value;
		this.type = CartValue.TypeValue.TEXT;
	}
	
	public CartValue() {
		this.value = null;
		this.type = CartValue.TypeValue.NONE;
	}
	
	public CartValue(int value) {
		this.value = value;
		this.type = CartValue.TypeValue.NUMBER;
	}

	public CartValue(boolean value) {
		this.value = value;
		this.type = CartValue.TypeValue.BOOL;
	}
	
	public boolean isString() {
		return this.type == CartValue.TypeValue.TEXT;
	}
	
	public boolean isNone() {
		return this.type == CartValue.TypeValue.NONE;
	}
	
	public boolean isInt() {
		return this.type == CartValue.TypeValue.NUMBER;
	}
	
	public boolean isBoolean() {
		return this.type == CartValue.TypeValue.BOOL;
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
	
	@Override
	public String toString() {
		if (type == TypeValue.NONE)
			return "none";
		return this.value.toString();
	}
	
	public Document toDocument() {
		return new Document("type", this.type.name()).append("value", this.value);
	}
	
	public static CartValue toCartValue(Document document) {
		TypeValue type = TypeValue.valueOf(document.getString("type"));
		switch (type) {
			case TEXT:
				return new CartValue(document.getString("value"));
			case NUMBER:
				return new CartValue(document.getInteger("value"));
			case BOOL:
				return new CartValue(document.getBoolean("value"));
			case NONE:
				return new CartValue();
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
