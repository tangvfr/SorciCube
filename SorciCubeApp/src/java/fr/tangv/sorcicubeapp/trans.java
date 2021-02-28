package fr.tangv.sorcicubeapp;

import java.io.IOException;

import fr.tangv.sorcicubecore.handler.HandlerPacketCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class trans {

	public static void main(String[] args) throws Exception {
		MongoDBManager mongo = new MongoDBManager(""
				, "plugin");
		ManagerPacketCards packets = new ManagerPacketCards(mongo);
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
					HandlerPacketCards pack = new HandlerPacketCards(this);
					for (String name : packets.getPackets()) {
						pack.newPacket(name);
						pack.updatePacket(name, packets.getPacketCards(name));
						System.out.println("trans: "+name);
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
