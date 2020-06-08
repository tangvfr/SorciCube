package fr.tangv.sorcicubespell.carts;

import org.bson.Document;

public class CartMaterial {

	private int id;
	private int data;
	private String url;
	
	public CartMaterial(String url) {
		this.url = url;
		this.id = 397;
		this.data = (int) 3;
	}
	
	public CartMaterial(int id, int data) {
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
	
	public static CartMaterial toCartMaterial(Document document) {
		String url = document.getString("url");
		if (url != null && !url.isEmpty())
			return new CartMaterial(url);
		else
			return new CartMaterial(document.getInteger("id"), document.getInteger("data"));
	}
	
}
