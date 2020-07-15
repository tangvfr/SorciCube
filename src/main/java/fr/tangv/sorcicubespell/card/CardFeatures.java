package fr.tangv.sorcicubespell.card;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

public class CardFeatures {

	private ConcurrentHashMap<CardFeatureType, CardFeature> features;
	
	public CardFeatures() {
		this.features = new ConcurrentHashMap<CardFeatureType, CardFeature>();
	}

	public boolean hasFeature(CardFeatureType type) {
		return this.features.containsKey(type);
	}
	
	public CardFeature getFeature(CardFeatureType type) {
		return this.features.get(type);
	}
	
	public CardFeature putFeature(CardFeature feature) {
		return this.features.put(feature.getType(), feature);
	}
	
	public CardFeature removeFeature(CardFeature feature) {
		return this.features.remove(feature.getType());
	}
	
	public Collection<CardFeature> valueFeatures() {
		return this.features.values();
	}
	
	public int size() {
		return this.features.size();
	}
	
	public Document toDocument() {
		Document document = new Document();
		document.append("version", 2);
		for (CardFeatureType key : this.features.keySet())
			document.append(key.name(), this.features.get(key).toDocument());
		return document;
	}
	
	public static CardFeatures toCardFeatures(Document document) {
		CardFeatures features = new CardFeatures();
		if (document.containsKey("version")) {
			for (String key : document.keySet()) {
				CardFeatureType type = CardFeatureType.valueOf(key);
				features.putFeature(CardFeature.toCartFeature(type, document.get(key, Document.class)));
			}
		} else {
			for (String key : document.keySet()) {
				Document docCard = document.get(key, Document.class);
				CardFeature feature = new CardFeature(
								CardFeatureType.valueOf(docCard.getString("type")),
								CardValue.toCartValue(docCard.get("value", Document.class))
							);
				if (!features.hasFeature(feature.getType()))
					features.putFeature(feature);
			}
		}
		return features;
	}
	
}
