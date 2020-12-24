package fr.tangv.sorcicubecore.card;

import org.bson.Document;

public class CardMaterial {

	private final int id;
	private final int data;
	private final CardSkin skin;
	private final boolean invalid;
	
	private CardMaterial() {
		this.invalid = true;
		this.skin = null;
		this.id = 397;
		this.data = (int) 3;
	}
	
	public CardMaterial(CardSkin skin) {
		this.invalid = false;
		this.skin = skin;
		this.id = 397;
		this.data = (int) 3;
	}
	
	public CardMaterial(int id, int data) {
		this.invalid = false;
		this.skin = null;
		this.id = id;
		this.data = data;
	}
	
	public boolean isInvalid() {
		return invalid;
	}
	
	public boolean hasSkin() {
		return skin != null;
	}
	
	public int getId() {
		return id;
	}

	public int getData() {
		return data;
	}

	public CardSkin getSkin() {
		return skin;
	}

	public Document toDocument() {
		if (isInvalid())
			return new Document();
		else if (hasSkin())
			return new Document().append("skin", skin.toDocument());
		else
			return new Document().append("id", id).append("data", data);
	}
	
	public static CardMaterial toCartMaterial(Document document) {
		if (document.containsKey("skin"))
			return new CardMaterial(CardSkin.toCartSkin(document.get("skin", Document.class)));
		else if (document.containsKey("id"))
			return new CardMaterial(document.getInteger("id"), document.getInteger("data"));
		else
			return new CardMaterial();
	}
	
	@Override
	public String toString() {
		if (isInvalid())
			return "invalid";
		if (hasSkin())
			return "skull: "+skin.toString();
		else
			return id+":"+data;
	}
	
}
