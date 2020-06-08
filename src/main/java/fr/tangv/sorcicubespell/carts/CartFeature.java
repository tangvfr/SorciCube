package fr.tangv.sorcicubespell.carts;

import org.bson.Document;

public class CartFeature {

	private String name;
	private CartFeatureType type;
	private CartCible cible;
	private CartValue value;
	
	public CartFeature(String name, CartFeatureType type, CartCible cible, CartValue value) {
		this.setName(name);
		this.setType(type);
		this.setCible(cible);
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

	public CartCible getCible() {
		return cible;
	}

	public void setCible(CartCible cible) {
		this.cible = cible;
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
				.append("cible", cible.name())
				.append("value", value.toDocument());
		return document;
	}
	
	public static CartFeature toCartFeature(Document document) {
		return new CartFeature(document.getString("name"),
				CartFeatureType.valueOf(document.getString("type")),
				CartCible.valueOf(document.getString("cible")),		
				CartValue.toCartValue(document.get("value", Document.class))
			);
	}
	
}
