package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.Enumeration;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.ramfiles.RamFilesManager;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerConfigYAML implements RequestHandlerInterface {

	private final RamFilesManager fm;
	
	public HandlerServerConfigYAML() throws IOException {
		this.fm = new RamFilesManager("./config_server");
	}

	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.CONFIG_SERVER_LIST)
	public void list(Client client, Request request) throws IOException, RequestException {
		String data = "";
		Enumeration<String> list = fm.list();
		while (list.hasMoreElements()) {
			data += list.nextElement();
			if (list.hasMoreElements())
				data += ";";
		}
		client.sendRequest(request.createReponse(RequestType.CONFIG_SERVER_LIST_CONFIG, data));
	}
	
	@RequestAnnotation(type=RequestType.CONFIG_SERVER_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			fm.update(request.name, request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CONFIG_SERVER_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			if (!fm.has(request.name))
				throw new Exception("Not exist config named \""+request.name+"\" !");
			client.sendRequest(request.createReponse(RequestType.CONFIG_SERVER_CONFIG, fm.get(request.name).getData()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
