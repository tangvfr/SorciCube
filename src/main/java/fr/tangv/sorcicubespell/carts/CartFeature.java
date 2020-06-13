package fr.tangv.sorcicubespell.carts;

import org.bson.Document;

public class CartFeature {

	private String name;
	private CartFeatureType type;
	private CartValue value;
	
	public CartFeature(String name, CartFeatureType type, CartValue value) {
		this.setName(name);
		this.setType(type);
		this.setValue(value);
	}
	
	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	public CartFeatureType getType() {
		return type;
	}

	public void setType(CartFeatureType type) {
		this.type = type;
	}

	public CartValue getValue() {
		return value;
	}

	public void setValue(CartValue value) {
		this.value = value;
	}

	public Document toDocument() {
		Document document = new Document()
				.append("name", name)
				.append("type", type.name())
				.append("value", value.toDocument());
		return document;
	}
	
	public static CartFeature toCartFeature(Document document) {
		return new CartFeature(document.getString("name"),
				CartFeatureType.valueOf(document.getString("type")),
				CartValue.toCartValue(document.get("value", Document.class))
			);
	}
	
}
