package fr.tangv.sorcicubecore.handler;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.lang.model.SourceVersion;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class HandlerCards {

	private final SorciClient sorci;
	private final ConcurrentHashMap<UUID, Card> cards;
	
	public HandlerCards(SorciClient sorci) throws IOException, ReponseRequestException, RequestException {
		this.cards = new ConcurrentHashMap<UUID, Card>();
		this.sorci = sorci;
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		this.cards.clear();
		Request reponse = sorci.sendRequestReponse(new Request(RequestType.CARDS_GET_ALL, Request.randomID(), "Cards", null),
				RequestType.CARDS_LIST);
		for (Document doc : Document.parse(reponse.data).getList("cards", Document.class)) {
			Card card = Card.toCard(doc);
			cards.put(card.getUUID(), card);
		}
	}
	
	public ConcurrentHashMap<UUID, Card> originalCards() {
		return cards;
	}
	
	public ConcurrentHashMap<UUID, Card> cloneCards() {
		return new ConcurrentHashMap<UUID, Card>(cards);
	}
	
	public Vector<Card> cloneCardsValue() {
		return new Vector<Card>(cards.values());
	}
	
	public Card getCard(UUID uuid) {
		return cards.get(uuid);
	}
	
	public void insert(Card card) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.CARDS_INSERT, Request.randomID(), card.getUUID().toString(), card.toDocument().toJson()),
				RequestType.SUCCESSFUL);
		cards.put(card.getUUID(), card);
	}
	
	public void update(Card card) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.CARDS_UPDATE, Request.randomID(), card.getUUID().toString(), card.toDocument().toJson()),
				RequestType.SUCCESSFUL);
		cards.replace(card.getUUID(), card);
	}
	
	public void delete(Card card) throws IOException, ReponseRequestException, RequestException {
		sorci.sendRequestReponse(new Request(RequestType.CARDS_DELETE, Request.randomID(), card.getUUID().toString(), card.toDocument().toJson()),
				RequestType.SUCCESSFUL);
		cards.remove(card.getUUID(), card);
	}
	
}
