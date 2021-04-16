package fr.tangv.sorcicubecore.handler;

import java.io.IOException;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.configs.Config;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerConfig {

	private final SorciClient client;
	private volatile Config config;
	
	public HandlerConfig(SorciClient client) throws IOException, ResponseRequestException, RequestException, ConfigParseException {
		this.client = client;
		refreshConfig();
	}
	
	public Config getConfig() {
		return config;
	}
	
	public void refreshConfig() throws IOException, ResponseRequestException, RequestException, ConfigParseException {
		Request response = client.sendRequestResponse(
				new Request(RequestType.CONFIG_GET, Request.randomID(), "get", null),
				RequestType.CONFIG_CONFIG
			);
		config = new Config(Document.parse(response.data));
	};
	
	public void uploadConfig() throws IOException, ResponseRequestException, RequestException, ConfigParseException {
		client.sendRequestResponse(
				new Request(RequestType.CONFIG_UPDATE, Request.randomID(), "update", config.toDocument().toJson()),
				RequestType.SUCCESSFUL
			);
	};
	
}
