package fr.tangv.sorcicubecore.handler.formater;

import org.bson.Document;

public interface HandlerObjectsFormater<K, V> {

	public String getType();
	public Document toDocument(V value);
	public K getKey(V value);
	public V toValue(Document doc);
	
}
