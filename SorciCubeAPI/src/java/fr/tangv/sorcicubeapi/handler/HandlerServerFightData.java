package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerFightData implements RequestHandlerInterface {

	private final ConcurrentHashMap<UUID, UUID> spectators;
	
	public HandlerServerFightData() {
		this.spectators = new ConcurrentHashMap<UUID, UUID>();
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	
	
	@RequestAnnotation(type=RequestType.SPECTATOR_ADD)
	public void add(Client client, Request request) throws IOException, RequestException {
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
	public void peek(Client client, Request request) throws IOException, RequestException {
		try {
			UUID player = UUID.fromString(request.name);
			UUID fight = spectators.remove(player);
			client.sendRequest(request.createReponse(RequestType.SPECTATOR_UUID, (fight == null) ? "null" : fight.toString()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
