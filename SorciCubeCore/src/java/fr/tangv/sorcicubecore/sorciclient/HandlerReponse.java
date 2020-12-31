package fr.tangv.sorcicubecore.sorciclient;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;

public class HandlerReponse implements RequestHandlerInterface {

	public final ConcurrentHashMap<Integer, Request> reponsesWait;
	public final SorciClient sorci;
	public final long timeout;
	
	public HandlerReponse(SorciClient sorci, long timeout) {
		this.reponsesWait = new ConcurrentHashMap<Integer, Request>();
		this.sorci = sorci;
		this.timeout = timeout;
	}

	@Override
	public void handlingRequest(Client client, Request reponse) throws Exception {
		Request request = reponsesWait.get(reponse.id);
		if (request != null) {
			reponsesWait.replace(reponse.id, reponse);
			request.notify();
		}
	}
	
	public Request sendRequestReponse(Request request) throws IOException {
		reponsesWait.put(request.id, request);
		sorci.sendRequest(request);
		try {
			request.wait(timeout);
		} catch (InterruptedException e) {
			return null;
		}
		Request reponse = reponsesWait.remove(request.id);
		if (request == reponse)
			return null;
		return reponse;
	}
	
}
