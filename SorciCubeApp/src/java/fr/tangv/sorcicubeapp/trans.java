package fr.tangv.sorcicubeapp;

import java.io.IOException;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class trans {

	public static void main(String[] args) throws Exception {
		MongoDBManager mongo = new MongoDBManager(""
				, "plugin");
		ManagerCards manage = new ManagerCards(mongo);
		SorciClientURI uri = new SorciClientURI("");
		SorciClient client = new SorciClient(uri, 5000) {
			
			@Override
			public void disconnected() {
				System.out.println("disconneted");
				System.exit(0);
			}
			
			@Override
			public void connected() {
				System.out.println("conneted");
				try {
					HandlerCards cards = new HandlerCards(this);
					for (Card card : manage.cloneCardsValue()) {
						System.out.println("Insert: "+card.getUUID().toString()+" "+card.renderName());
						cards.insert(card);
					}
				} catch (IOException | ReponseRequestException | RequestException e) {
					e.printStackTrace();
				}
				this.disconnect();
			}
			
		};
		client.setPrintStream(System.out);
		client.start();
	}
	
}
