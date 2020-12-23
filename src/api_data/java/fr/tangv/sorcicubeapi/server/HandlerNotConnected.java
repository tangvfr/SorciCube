package fr.tangv.sorcicubeapi.server;

import org.bson.Document;

import fr.tangv.sorcicubeapi.clients.Client;
import fr.tangv.sorcicubeapi.clients.ClientIdentification;
import fr.tangv.sorcicubeapi.requests.Request;
import fr.tangv.sorcicubeapi.requests.RequestException;
import fr.tangv.sorcicubeapi.requests.RequestHandlerInterface;
import fr.tangv.sorcicubeapi.requests.RequestType;

public class HandlerNotConnected implements RequestHandlerInterface {

	private final Request dontAuthentified;
	private final ClientsManager manager;
	
	public HandlerNotConnected(ClientsManager manager) throws RequestException {
		this.dontAuthentified = new Request(RequestType.DONT_AUTHENTIFIED, "This action is invalid, you dont are authentified !", "");
		this.manager = manager;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		if (request.typeRequest == RequestType.IDENTIFICATION) {
			ClientIdentification clientID = ClientIdentification.toClientIdentification(Document.parse(request.data));
			if (!clientID.isValid()) {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, "ClientIdentification", "ClientIdentification is wrong !"));
			} else if (!Client.VERSION_PROTOCOL.equals(clientID.version)) {
				client.setClientID(clientID);
				if (manager.authentification(client)) {
					client.sendRequest(new Request(RequestType.AUTHENTIFIED, clientID.name, ""));
				} else {
					client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED,	"Authentification", "Token is wrong"));
					client.close();
				}
			} else {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, "VersionProtocol", 
												"Version of protocol is invalid, server version is \""
												+Client.VERSION_PROTOCOL+
												"\" and your client version is \""
												+clientID.version+
												"\"."));
			}
		} else {
			client.sendRequest(dontAuthentified);
		}
	}

}
