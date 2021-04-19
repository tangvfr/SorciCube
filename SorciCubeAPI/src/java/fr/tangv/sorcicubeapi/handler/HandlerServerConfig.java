package fr.tangv.sorcicubeapi.handler;

import java.io.File;
import java.io.IOException;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.configs.Config;
import fr.tangv.sorcicubecore.ramfiles.RamFile;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerServerConfig implements RequestHandlerInterface {

	private final RamFile file;
	private volatile Config config;
	
	public HandlerServerConfig() throws IOException, ConfigParseException {
		this.file = new RamFile(new File("./config.json"));
		file.loadData();
		String data = file.getData();
		if (!data.isEmpty()) {
			config = new Config(Document.parse(data));
		} else {
			config = new Config(null);
			file.writeData(config.toDocument().toJson());
		}
	}

	public Config getConfig() {
		return config;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {}
	
	@RequestAnnotation(type=RequestType.CONFIG_UPDATE)
	public void update(Client client, Request request) throws IOException, RequestException {
		try {
			config = new Config(Document.parse(request.data));
			file.writeData(request.data);
			client.sendRequest(request.createReponse(RequestType.SUCCESSFUL, null));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
	@RequestAnnotation(type=RequestType.CONFIG_GET)
	public void get(Client client, Request request) throws IOException, RequestException {
		try {
			client.sendRequest(request.createReponse(RequestType.CONFIG_CONFIG, file.getData()));
		} catch (Exception e) {
			client.sendRequest(request.createReponse(RequestType.ERROR, e.getMessage()));
		}
	}
	
}
