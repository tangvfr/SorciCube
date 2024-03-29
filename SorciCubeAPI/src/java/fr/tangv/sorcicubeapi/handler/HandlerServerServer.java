package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;

import fr.tangv.sorcicubeapi.SorciCubeAPI;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;
import fr.tangv.sorcicubecore.clients.ClientType;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerServer implements RequestHandlerInterface {

	private final SorciCubeAPI api;
	
	public HandlerServerServer(SorciCubeAPI api) {
		this.api = api;
	}

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.START_SERVERS_REFRESH)
	public void startRefresh(Client client, Request request) throws IOException, RequestException {
		Request refresh = new Request(RequestType.SERVER_REFRESH, Request.randomID(), "Refresh", null);
		for (Client cl : api.getClientsManager().clients)
			if (ClientType.SPIGOT.isType(cl.getClientID().types))
				cl.sendRequest(refresh);
		client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));	
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_JOIN)
	public void playerJoin(Client client, Request request) throws IOException, RequestException {
		client.setValue((int) client.getValue()+1);
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_LEAVE)
	public void playerLeave(Client client, Request request) throws IOException, RequestException {
		client.setValue((int) client.getValue()-1);
	}
	
	@RequestAnnotation(type=RequestType.STOP_SERVER)
	public void stopServer(Client client, Request request) throws IOException, RequestException {
		client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		api.stopServer();
	}
	
	@RequestAnnotation(type=RequestType.GET_SPITGOT_SERVER_LIST)
	public void spigotServerList(Client client, Request request) throws IOException, RequestException {
		ArrayList<Document> list = new ArrayList<Document>();
		for (Client cl : api.getClientsManager().clients) {
			ClientIdentification id = cl.getClientID();
			if (ClientType.SPIGOT.isType(id.types))
				list.add(new Document()
						.append("name", id.name)
						.append("time_connected", Long.toString(cl.calcTimeConnected(), 16))
						.append("players", (int) cl.getValue())
					);
		}
		client.sendRequest(request.createReponse(RequestType.SPITGOT_SERVER_LIST, new Document("list", list).toJson()));
	}
	
}
