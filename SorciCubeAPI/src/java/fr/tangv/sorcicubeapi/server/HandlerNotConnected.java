package fr.tangv.sorcicubeapi.server;

import org.bson.Document;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;
import fr.tangv.sorcicubecore.requests.RequestType;

public class HandlerNotConnected implements RequestHandlerInterface {

	private final ClientsManager manager;
	
	public HandlerNotConnected(ClientsManager manager) throws RequestException {
		this.manager = manager;
	}
	
	@Override
	public void handlingRequest(Client client, Request request) throws Exception {
		System.out.println(client.getInetAddress().getHostAddress()+" <tryauth< "+request.toRequestNoData());
		if (request.requestType == RequestType.IDENTIFICATION) {
			ClientIdentification clientID = ClientIdentification.toClientIdentification(Document.parse(request.data));
			if (!clientID.isValid()) {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, request.id, "ClientIdentification", "ClientIdentification is wrong !"));
			} else if (Client.VERSION_PROTOCOL.equals(clientID.version)) {
				client.setClientID(clientID);
				if (manager.authentification(client)) {
					client.sendRequest(new Request(RequestType.AUTHENTIFIED, request.id, clientID.name, ""));
					System.out.println(client.getInetAddress().getHostAddress()+":"+client.getClientID().name+":"+client.getClientID().token+" <auth< "+"sucessful");
					return;
				} else {
					client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED,request.id, "Authentification", "Token is wrong"));
				}
			} else {
				client.sendRequest(new Request(RequestType.IDENTIFICATION_REFUSED, request.id, "ProtocolVersion", 
												"ProtocolVersion is invalid, server version is \""
												+Client.VERSION_PROTOCOL+
												"\" and your client version is \""
												+clientID.version+
												"\"."));
			}
			client.close();
		} else {
			System.out.println(client.getInetAddress().getHostAddress()+" <auth< "+"wrong");
			client.sendRequest(new Request(RequestType.DONT_AUTHENTIFIED, request.id, "NotAuthentified" ,"This action is invalid, you dont are authentified !"));
		}
	}

}
