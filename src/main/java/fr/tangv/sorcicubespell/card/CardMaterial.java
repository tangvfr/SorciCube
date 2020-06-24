package fr.tangv.sorcicubespell.card;

import org.bson.Document;

public class CardMaterial {

	private int id;
	private int data;
	private String url;
	
	public CardMaterial(String url) {
		this.url = url;
		this.id = 397;
		this.data = (int) 3;
	}
	
	public CardMaterial(int id, int data) {
		this.url = null;
		this.id = id;
		this.data = data;
	}
	
	public boolean hasUrl() {
		return url != null && !url.isEmpty();
	}
	
	public int getId() {
		return id;
	}

	public int getData() {
		return data;
	}

	public String getUrl() {
		return url;
	}

	public Document toDocument() {
		Document document = new Document()
				.append("id", id)
				.append("data", data)
				.append("url", url);
		return document;
	}
	
	public static CardMaterial toCartMaterial(Document document) {
		String url = document.getString("url");
		if (url != null && !url.isEmpty())
			return new CardMaterial(url);
		else
			return new CardMaterial(document.getInteger("id"), document.getInteger("data"));
	}
	
	@Override
	public String toString() {
		if (hasUrl())
			return "skull: "+url;
		else
			return id+":"+data;
	}
	
}
