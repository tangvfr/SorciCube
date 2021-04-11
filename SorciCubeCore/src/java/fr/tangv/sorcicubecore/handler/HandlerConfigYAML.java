package fr.tangv.sorcicubecore.handler;

import java.io.IOException;

import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerConfigYAML {

	private final SorciClient sorci;
	
	public HandlerConfigYAML(SorciClient sorci) {
		this.sorci = sorci;
	}
	
	public String[] listConfig() throws IOException, ResponseRequestException, RequestException {
		Request reponse = sorci.sendRequestResponse(
				new Request(RequestType.CONFIG_SERVER_LIST, Request.randomID(), "List", null),
				RequestType.CONFIG_SERVER_LIST_CONFIG
			);
		return reponse.data.split(";");
	}
	
	public String getConfig(String name) throws IOException, ResponseRequestException, RequestException {
		Request reponse = sorci.sendRequestResponse(
				new Request(RequestType.CONFIG_SERVER_GET, Request.randomID(), name, null),
				RequestType.CONFIG_SERVER_CONFIG
			);
		return reponse.data;
	};
	
	public void updateConfig(String name, String data) throws IOException, ResponseRequestException, RequestException {
		sorci.sendRequestResponse(
				new Request(RequestType.CONFIG_SERVER_UPDATE, Request.randomID(), name, data),
				RequestType.SUCCESSFUL
			);
	};
	
}
