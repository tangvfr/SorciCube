package fr.tangv.sorcicubeapp;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class trans {

	public static void main(String[] args) throws Exception {
		MongoDBManager mongo = new MongoDBManager(""
				, "");
		ManagerCards manage = new ManagerCards(mongo);
		SorciClientURI uri = new SorciClientURI("");
		HandlerCards cards = new HandlerCards(new SorciClient(uri, 5000) {
			
			@Override
			public void disconnected() {
				
			}
			
			@Override
			public void connected() {
				
			}
			
		});
		for (Card card : manage.cloneCardsValue())
			cards.insert(card);
		System.exit(0);
	}
	
}
