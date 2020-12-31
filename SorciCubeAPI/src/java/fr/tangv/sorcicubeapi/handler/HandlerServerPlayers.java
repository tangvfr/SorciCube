package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.files.FileManager;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerPlayers implements RequestHandlerInterface {

	public FileManager fm;
	
	public HandlerServerPlayers() throws IOException {
		this.fm = new FileManager("./players");
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.PLAYER_INIT)
	public void init(Client client, Request request) throws IOException, RequestException {
		try {
			fm.insert(request.name, request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (IOException e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			fm.update(request.name, request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (IOException e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.PLAYER_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.PLAYER_REPONSE, fm.get(request.name).getData()));
		} catch (IOException e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}

	@RequestAnnotation(type=RequestType.PLAYER_EXIST)
	public void exist(Client client, Request request) throws IOException, RequestException {
		client.sendRequest(request.createReponse(RequestType.PLAYER_EXITSTED, Boolean.toString(fm.has(request.name))));
	}
	
}
