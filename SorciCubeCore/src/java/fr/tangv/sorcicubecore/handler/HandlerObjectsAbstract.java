package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import fr.tangv.sorcicubecore.handler.formater.HandlerObjectsFormater;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public abstract class HandlerObjectsAbstract<K, V> {

	private final SorciClient sorci;
	protected final ConcurrentHashMap<K, V> map;
	private final HandlerObjectsFormater<K, V> formater;
	
	public HandlerObjectsAbstract(SorciClient sorci, HandlerObjectsFormater<K, V> formater) throws IOException, ReponseRequestException, RequestException {
		this.map = new ConcurrentHashMap<K, V>();
		this.sorci = sorci;
		this.formater = formater;
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		this.map.clear();
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.OBJECTS_GET_ALL, Request.randomID(), formater.getType(), null),
				RequestType.OBJECTS_LIST);
		for (Document doc : Document.parse(reponse.data).getList("list", Document.class)) {
			V value = formater.toValue(doc);
			map.put(formater.getKey(value), value);
		}
	}
	
	public ConcurrentHashMap<K, V> originalMap() {
		return map;
	}
	
	public ConcurrentHashMap<K, V> cloneMap() {
		return new ConcurrentHashMap<K, V>(map);
	}
	
	public Vector<V> cloneValues() {
		return new Vector<V>(map.values());
	}
	
	public V get(K key) {
		return map.get(key);
	}
	
	public void add(V value) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.OBJECTS_ADD, Request.randomID(), formater.getType(), formater.toDocument(value).toJson()),
				RequestType.SUCCESSFUL);
		map.put(formater.getKey(value), value);
	}
	
	public void update(V value) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.OBJECTS_UPDATE, Request.randomID(), formater.getType(), formater.toDocument(value).toJson()),
				RequestType.SUCCESSFUL);
		map.replace(formater.getKey(value), value);
	}
	
	public void remove(V value) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.OBJECTS_REMOVE, Request.randomID(), formater.getType(), formater.toDocument(value).toJson()),
				RequestType.SUCCESSFUL);
		map.remove(formater.getKey(value), value);
	}
	
}
