package fr.tangv.sorcicubeapi.server;

import org.bson.Document;

import fr.tangv.sorcicubeapi.clients.Client;
import fr.tangv.sorcicubeapi.clients.ClientIdentification;
import fr.tangv.sorcicubeapi.requests.Request;
import fr.tangv.sorcicubeapi.requests.RequestException;
import fr.tangv.sorcicubeapi.requests.RequestHandlerInterface;
import fr.tangv.sorcicubeapi.requests.RequestType;

public class RequestHandlerDontConnected implements RequestHandlerInterface {

	private final Request dontAuthentified;
	private final ClientManager manager;
	
	public RequestHandlerDontConnected(ClientManager manager) throws RequestException {
		this.dontAuthentified = new Request(RequestType.DONT_AUTHENTIFIED, "This action is invalid, you dont are authentified !", "");
		this.manager = manager;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		if (request.typeRequest == RequestType.IDENTIFICATION) {
			ClientIdentification clientID = ClientIdentification.toClientIdentification(Document.parse(request.data));
			if (!clientID.isValid()) {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, "ClientIdentification", "ClientIdentification is wrong !"));
			} else if (!ClientManager.VERSION_PROTOCOL.equals(clientID.version)) {
				client.setClientID(clientID);
				manager.authentification(client);
			} else {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, "VersionProtocol", 
												"Version of protocol is invalid, server version is \""
												+ClientManager.VERSION_PROTOCOL+
												"\" and your client version is \""
												+clientID.version+
												"\"."));
			}
		} else {
			client.sendRequest(dontAuthentified);
		}
	}

}
