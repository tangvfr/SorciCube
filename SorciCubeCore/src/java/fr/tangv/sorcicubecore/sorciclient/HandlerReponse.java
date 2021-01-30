package fr.tangv.sorcicubecore.sorciclient;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;

public class HandlerReponse implements RequestHandlerInterface {

	public final ConcurrentHashMap<Integer, ReponseRequest> reponsesWait;
	public final SorciClient sorci;
	public final long timeout;
	
	public HandlerReponse(SorciClient sorci, long timeout) {
		this.reponsesWait = new ConcurrentHashMap<Integer, ReponseRequest>();
		this.sorci = sorci;
		this.timeout = timeout;
	}

	@Override
	public void handlingRequest(Client client, Request reponse) throws Exception {
		ReponseRequest rep = reponsesWait.remove(reponse.id);
		if (rep != null)
			rep.reponseRequest(reponse);
	}
	
	public Request sendRequestReponse(Request request) throws IOException {
		ReponseRequest rep =  new ReponseRequest(request);
		reponsesWait.put(request.id, rep);
		return rep.sendRequestReponse();
	}
	
	private class ReponseRequest {
		
		private final Request request;
		private volatile Request reponse;
		
		public ReponseRequest(Request request) {
			this.request = request;
			this.reponse = null;
		}
		
		public synchronized void reponseRequest(Request reponse) throws Exception {
			this.reponse = reponse;
			this.notifyAll();
		}
		
		public synchronized Request sendRequestReponse() throws IOException {
			sorci.sendRequest(request);
			try {
				this.wait(timeout);
			} catch (InterruptedException e) {
				return null;
			}
			return reponse;
		}
		
	}
	
}
