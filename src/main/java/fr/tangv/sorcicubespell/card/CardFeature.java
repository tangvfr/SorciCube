package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardFeature {

	private String name;
	private CardFeatureType type;
	private CardValue value;
	
	public CardFeature(String name, CardFeatureType type, CardValue value) {
		this.setName(name);
		this.setType(type);
		if (value.getType() == type.getTypeValue())
			this.setValue(value);
		else
			this.setValue(CardValue.createCardValue(type.getTypeValue()));
	}
	
	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}

	public CardFeatureType getType() {
		return type;
	}

	public void setType(CardFeatureType type) {
		this.type = type;
	}

	public CardValue getValue() {
		return value;
	}

	public void setValue(CardValue value) {
		this.value = value;
	}

	public Document toDocument() {
		Document document = new Document()
				.append("name", name)
				.append("type", type.name())
				.append("value", value.toDocument());
		return document;
	}
	
	public static CardFeature toCartFeature(Document document) {
		return new CardFeature(document.getString("name"),
				CardFeatureType.valueOf(document.getString("type")),
				CardValue.toCartValue(document.get("value", Document.class))
			);
	}
	
}
