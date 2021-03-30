package fr.tangv.sorcicubecore.card;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.bson.Document;

import fr.tangv.sorcicubecore.handler.HandlerCards;

public class CardFeatures {

	private final ConcurrentHashMap<CardFeatureType, CardFeature> features;
	
	public CardFeatures() {
		this.features = new ConcurrentHashMap<CardFeatureType, CardFeature>();
	}

	public boolean hasFeature(CardFeatureType type) {
		return this.features.containsKey(type);
	}
	
	public CardFeature getFeature(CardFeatureType type) {
		return this.features.get(type);
	}
	
	public void putFeature(CardFeature feature) {
		this.features.put(feature.getType(), feature);
	}
	
	public void removeFeature(CardFeature feature) {
		removeFeature(feature.getType());
	}
	
	public void removeFeature(CardFeatureType type) {
		this.features.remove(type);
	}
	
	public Collection<CardFeature> valueFeatures() {
		return this.features.values();
	}
	
	public KeySetView<CardFeatureType, CardFeature> keySet() {
		return this.features.keySet();
	}
	
	public int size() {
		return this.features.size();
	}
	
	public boolean hasNUUID(HandlerCards handler) {
		for (CardFeature fea : features.values())
			if (fea.isNUUID(handler))
				return true;
		return false;
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
				if (key.equals("version"))
					continue;
				features.putFeature(CardFeature.toCartFeature(CardFeatureType.valueOf(key), document.get(key, Document.class)));
			}
		}
		return features;
	}
	
}
