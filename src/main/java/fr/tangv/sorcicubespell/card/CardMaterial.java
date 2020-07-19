package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardMaterial {

	private int id;
	private int data;
	private CardSkin skin;
	
	public CardMaterial(CardSkin skin) {
		this.skin = skin;
		this.id = 397;
		this.data = (int) 3;
	}
	
	public CardMaterial(int id, int data) {
		this.skin = null;
		this.id = id;
		this.data = data;
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
		if (hasSkin())
			return new Document().append("skin", skin.toDocument());
		else
			return new Document().append("id", id).append("data", data);
	}
	
	public static CardMaterial toCartMaterial(Document document) {
		if (document.containsKey("skin")) {
			return new CardMaterial(CardSkin.toCartSkin(document.get("skin", Document.class)));
		}
		if (document.containsKey("url")) {
			String url = document.getString("url");
			if (url != null && !url.isEmpty())
				return new CardMaterial(new CardSkin(url));
		}
		return new CardMaterial(document.getInteger("id"), document.getInteger("data"));
	}
	
	@Override
	public String toString() {
		if (hasSkin())
			return "skull: "+skin.toString();
		else
			return id+":"+data;
	}
	
}
