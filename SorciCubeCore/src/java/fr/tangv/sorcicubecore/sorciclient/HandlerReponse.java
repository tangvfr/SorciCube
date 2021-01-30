package fr.tangv.sorcicubecore.sorciclient;

import java.io.IOException;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestHandlerInterface;

public class HandlerReponse implements RequestHandlerInterface {

	//public final ConcurrentHashMap<Integer, Request> reponsesWait;
	public final SorciClient sorci;
	public final long timeout;
	private volatile Request request;
	private volatile Request reponse;
	
	public HandlerReponse(SorciClient sorci, long timeout) {
		//this.reponsesWait = new ConcurrentHashMap<Integer, Request>();
		this.sorci = sorci;
		this.timeout = timeout;
	}

	@Override
	public synchronized void handlingRequest(Client client, Request reponse) throws Exception {
		//System.out.println("RID: "+reponse.id);
		/*
		Request request = reponsesWait.get(reponse.id);
		if (request != null) {
			reponsesWait.replace(reponse.id, reponse);
			request.name.notifyAll();
		}*/
		if (request != null) {
			System.out.println("RID: "+reponse.id);
			
			if (reponse.id == request.id) {
				this.reponse = reponse;
				this.notifyAll();
			}
		}
	}
	
	public synchronized Request sendRequestReponse(Request request) throws IOException {
		this.reponse = null;
		this.request = request;
		//reponsesWait.put(request.id, request);
		sorci.sendRequest(request);
		try {
			this.wait(timeout);
		} catch (InterruptedException e) {
			return null;
		}
		System.out.println("R<RID: "+request.id);
		this.request = null;
		return reponse;
	}
	
}
