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
						.append("time_connected", cl.calcTimeConnected())
					);
		}
		client.sendRequest(request.createReponse(RequestType.SPITGOT_SERVER_LIST, new Document("list", list).toJson()));
	}
	
}
