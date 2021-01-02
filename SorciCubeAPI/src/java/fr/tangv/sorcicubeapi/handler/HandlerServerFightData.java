package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerFightData implements RequestHandlerInterface {

	private final ConcurrentHashMap<UUID, UUID> spectators;
	private final Vector<Document> fightDatas;
	
	public HandlerServerFightData() {
		this.spectators = new ConcurrentHashMap<UUID, UUID>();
		this.fightDatas = new Vector<Document>();
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_GET_LIST)
	public void getList(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.FIGHT_DATA_GET_LIST, new Document("list", fightDatas).toString()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_ADD)
	public void add(Client client, Request request) throws IOException, RequestException {
		try {
			fightDatas.add(Document.parse(request.data));
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_REMOVE)
	public void remove(Client client, Request request) throws IOException, RequestException {
		try {
			fightDatas.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document d) {
					return d.getString("fight_uuid").equals(request.name);
				}
			});
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			fightDatas.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document d) {
					return d.getString("fight_uuid").equals(request.name);
				}
			});
			fightDatas.add(Document.parse(request.data));
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_PLAYER_GET)
	public void getPlayer(Client client, Request request) throws IOException, RequestException {
		try {
			Document doc = null;
			for (Document d : fightDatas)
				if (d.getString("player1").equals(request.name) || d.getString("player2").equals(request.name)) {
					doc = d;
					break;
				}
			client.sendRequest(request.createReponse(RequestType.FIGHT_DATA_ONE, (doc == null) ? "null" : doc.toJson()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_PLAYER_REMOVE)
	public void removePlayer(Client client, Request request) throws IOException, RequestException {
		try {
			fightDatas.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document d) {
					return d.getString("player1").equals(request.name) || d.getString("player2").equals(request.name);
				}
			});
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.FIGHT_DATA_SERVER_REMOVE)
	public void removeServer(Client client, Request request) throws IOException, RequestException {
		try {
			fightDatas.removeIf(new Predicate<Document>() {
				@Override
				public boolean test(Document d) {
					return d.getString("server").equals(request.name);
				}
			});
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.SPECTATOR_ADD)
	public void addSpectator(Client client, Request request) throws IOException, RequestException {
		try {
			UUID player = UUID.fromString(request.name);
			UUID fight = UUID.fromString(request.data);
			spectators.remove(player);
			spectators.put(player, fight);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}

	@RequestAnnotation(type=RequestType.SPECTATOR_PEEK)
	public void peekSpectator(Client client, Request request) throws IOException, RequestException {
		try {
			UUID player = UUID.fromString(request.name);
			UUID fight = spectators.remove(player);
			client.sendRequest(request.createReponse(RequestType.SPECTATOR_UUID, (fight == null) ? "null" : fight.toString()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
