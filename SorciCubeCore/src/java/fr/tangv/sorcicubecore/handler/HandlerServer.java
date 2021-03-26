package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerServer {

	private final SorciClient client;
	
	public HandlerServer(SorciClient client) {
		this.client = client;
	}
	
	public void startRefresh() throws IOException, ReponseRequestException, RequestException {
		client.sendRequestReponse(
				new Request(RequestType.START_SERVERS_REFRESH, Request.randomID(), "RefreshServers", null),
				RequestType.SUCCESSFUL
			);
	}
	
	public void playerJoin(UUID uuid) throws IOException, RequestException {
		client.sendRequest(new Request(RequestType.PLAYER_JOIN, Request.randomID(), uuid.toString(), null));
	}
	
	public void playerLeave(UUID uuid) throws IOException, RequestException {
		client.sendRequest(new Request(RequestType.PLAYER_LEAVE, Request.randomID(), uuid.toString(), null));
	}
	
	public void stopServer() throws IOException, ReponseRequestException, RequestException {
		client.sendRequestReponse(
				new Request(RequestType.STOP_SERVER, Request.randomID(), "StopServer", null),
				RequestType.SUCCESSFUL
			);
	}
	
	public List<Document> getSpigotServerList() throws IOException, ReponseRequestException, RequestException {
		Request request = client.sendRequestReponse(
				new Request(RequestType.GET_SPITGOT_SERVER_LIST, Request.randomID(), "GetSpigotServerList", null),
				RequestType.SPITGOT_SERVER_LIST
			);
		return Document.parse(request.data).getList("list", Document.class);
	}
	
}
