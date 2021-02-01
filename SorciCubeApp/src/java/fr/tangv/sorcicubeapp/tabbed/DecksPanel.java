package fr.tangv.sorcicubeapp.tabbed;

import java.io.IOException;

import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubecore.handler.HandlerDefaultDeck;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;

public class DecksPanel extends JTabbedPane {

	private static final long serialVersionUID = 1955809174823917847L;
	private final HandlerDefaultDeck decks;
	private final DeckPanel dark;
	private final DeckPanel light;
	private final DeckPanel nature;
	private final DeckPanel toxic;
	
	public DecksPanel(CardsPanel cardsPanel) throws IOException, ReponseRequestException, RequestException, DeckException {
		this.decks = new HandlerDefaultDeck(cardsPanel.getClient(), cardsPanel.getCards());
		this.setTabPlacement(JTabbedPane.LEFT);
		//init decks
		this.dark = new DeckPanel(cardsPanel);
		this.light = new DeckPanel(cardsPanel);
		this.nature = new DeckPanel(cardsPanel);
		this.toxic = new DeckPanel(cardsPanel);
		//show deck
		refresh();
		//add decks
		this.addTab("Dark", dark);
		this.addTab("Light", light);
		this.addTab("Nature", nature);
		this.addTab("Toxic", toxic);
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException, DeckException {
		decks.refresh();
		this.dark.setDeck(decks.getDeckDark());
		this.light.setDeck(decks.getDeckLight());
		this.nature.setDeck(decks.getDeckNature());
		this.toxic.setDeck(decks.getDeckToxic());
	}
	
}
