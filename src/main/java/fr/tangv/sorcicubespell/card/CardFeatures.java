package fr.tangv.sorcicubespell.card;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

public class CardFeatures {

	private Map<String, CardFeature> features;
	
	public CardFeatures() {
		this.features = new HashMap<String, CardFeature>();
	}

	public boolean hasFeature(String name) {
		return this.features.containsKey(name);
	}
	
	public CardFeature getFeature(String name) {
		return this.features.get(name);
	}
	
	public void renameFeature(CardFeature cartFeature, String newName) {
		this.removeFeature(cartFeature.getName());
		cartFeature.setName(newName);
		this.putFeature(cartFeature);
	}
	
	public void replaceFeature(CardFeature cartFeature) {
		this.features.replace(cartFeature.getName(), cartFeature);
	}
	
	public void putFeature(CardFeature cartFeature) {
		this.features.put(cartFeature.getName(), cartFeature);
	}
	
	public void removeFeature(String name) {
		this.features.remove(name);
	}
	
	public Set<String> listNameFeatures() {
		return this.features.keySet();
	}
	
	public Collection<CardFeature> listFeatures() {
		return this.features.values();
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
	
	public static CardFeatures toCartFeatures(Document document) {
		CardFeatures features = new CardFeatures();
		for (String key : document.keySet())
			features.putFeature(CardFeature.toCartFeature(document.get(key, Document.class)));
		return features;
	}
	
}