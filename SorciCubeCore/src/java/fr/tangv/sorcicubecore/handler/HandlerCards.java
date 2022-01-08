package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.handler.formater.HandlerCardsFormater;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerCards extends HandlerObjectsAbstract<UUID, Card> {

	public HandlerCards(SorciClient client) throws IOException, ResponseRequestException, RequestException {
		super(client, new HandlerCardsFormater());
	}
	
}
