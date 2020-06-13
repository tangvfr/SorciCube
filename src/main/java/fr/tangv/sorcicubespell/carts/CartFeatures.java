package fr.tangv.sorcicubespell.carts;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

public class CartFeatures {

	private Map<String, CartFeature> features;
	
	public CartFeatures() {
		this.features = new HashMap<String, CartFeature>();
	}

	public boolean hasFeature(String name) {
		return this.features.containsKey(name);
	}
	
	public CartFeature getFeature(String name) {
		return this.features.get(name);
	}
	
	public void renameFeature(CartFeature cartFeature, String newName) {
		this.removeFeature(cartFeature.getName());
		cartFeature.setName(newName);
		this.putFeature(cartFeature);
	}
	
	public void replaceFeature(CartFeature cartFeature) {
		this.features.replace(cartFeature.getName(), cartFeature);
	}
	
	public void putFeature(CartFeature cartFeature) {
		this.features.put(cartFeature.getName(), cartFeature);
	}
	
	public void removeFeature(String name) {
		this.features.remove(name);
	}
	
	public int size() {
		return this.features.size();
	}
	
	public Document toDocument() {
		Document document = new Document();
		for (String key : this.features.keySet())
			document.append(key, this.features.get(key).toDocument());
		return document;
	}
	
	public static CartFeatures toCartFeatures(Document document) {
		CartFeatures features = new CartFeatures();
		for (String key : document.keySet())
			features.putFeature(CartFeature.toCartFeature(document.get(key, Document.class)));
		return features;
	}
	
}
