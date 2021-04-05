package fr.tangv.sorcicubeapi.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.handler.formater.HandlerObjectsFormater;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public abstract class HandlerServerObjectsAbstract<K, V> implements RequestHandlerInterface {

		protected final ConcurrentHashMap<K, V> map;
		private final File file;
		private final HandlerObjectsFormater<K, V> formater;
		
		public HandlerServerObjectsAbstract(HandlerObjectsFormater<K, V> formater) throws IOException {
			this.file = new File("./"+formater.getType()+".json");
			this.map = new ConcurrentHashMap<K, V>();
			this.formater = formater;
			load();
		}
		
		public synchronized void load() throws IOException {
			if (!file.exists()) {
				file.createNewFile();
				save();
			} else {
				if (file.isFile()) {
					FileInputStream in = new FileInputStream(file);
					byte[] buf = new byte[(int) file.length()];
					in.read(buf);
					in.close();
					Document d = Document.parse(new String(buf, Client.CHARSET));
					buf = new byte[0];
					map.clear();
					for (Document doc : d.getList("map", Document.class)) {
						V value = formater.toValue(doc);
						map.put(formater.getKey(value), value);
					}
				} else
					throw new IOException("Error not file, \""+file.getName()+"\" is directory !");
			}
		}
		
		public synchronized void save() throws IOException {
			ArrayList<Document> list = new ArrayList<Document>();
			for (V value : map.values())
				list.add(formater.toDocument(value));
			FileOutputStream out = new FileOutputStream(file);
			out.write(new Document("map", list).toJson().getBytes(Client.CHARSET));
			out.close();
		}
		
		public ConcurrentHashMap<K, V> getMap() {
			return map;
		}
		
		@Override
		public void handlingRequest(Client client, Request request) throws Exception {
			if (request.name.equals(formater.getType())) {
				try {
					V value;
					switch (request.requestType) {
					
						case OBJECTS_ADD:
							value = formater.toValue(Document.parse(request.data));
							map.put(formater.getKey(value), value);
							save();
							client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
							break;
					
						case OBJECTS_UPDATE:
							value = formater.toValue(Document.parse(request.data));
							map.replace(formater.getKey(value), value);
							save();
							client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
							break;
							
						case OBJECTS_REMOVE:
							value = formater.toValue(Document.parse(request.data));
							map.remove(formater.getKey(value));
							save();
							client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
							break;
							
						case OBJECTS_GET_ALL:
							ArrayList<Document> list = new ArrayList<Document>();
							for (V v : map.values())
								list.add(formater.toDocument(v));
							client.sendRequest(request.createReponse(RequestType.OBJECTS_LIST, new Document("list", list).toJson()));
							break;
		
						default:
							break;
					}
				} catch (Exception e) {
					client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
				}
			}
		}
	
}
