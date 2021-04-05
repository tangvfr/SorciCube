package fr.tangv.sorcicubeapi.handler;

import java.io.IOException;
import java.util.UUID;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.handler.formater.HandlerCardsFormater;

public class HandlerServerCards extends HandlerServerObjectsAbstract<UUID, Card> {

	public HandlerServerCards() throws IOException {
		super(new HandlerCardsFormater());
	}
	
}
