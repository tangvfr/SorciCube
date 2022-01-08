package fr.tangv.sorcicubeapp.tabbed;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubecore.handler.HandlerDefaultDeck;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;

public class DecksPanel extends JTabbedPane {

	private static final long serialVersionUID = 1955809174823917847L;
	private final HandlerDefaultDeck decks;
	private final DeckPanel dark;
	private final DeckPanel light;
	private final DeckPanel nature;
	private final DeckPanel toxic;
	
	public DecksPanel(CardsPanel cardsPanel) throws IOException, ResponseRequestException, RequestException, DeckException {
		this.decks = new HandlerDefaultDeck(cardsPanel.getClient(), cardsPanel.getCards());
		this.setTabPlacement(JTabbedPane.LEFT);
		//init decks
		this.dark = new DeckPanel(this, cardsPanel);
		this.light = new DeckPanel(this, cardsPanel);
		this.nature = new DeckPanel(this, cardsPanel);
		this.toxic = new DeckPanel(this, cardsPanel);
		//show deck
		refresh();
		//add decks
		this.addTab("Dark", dark);
		this.addTab("Light", light);
		this.addTab("Nature", nature);
		this.addTab("Toxic", toxic);
		this.addTab("Refresh", new JPanel());
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (DecksPanel.this.getSelectedIndex() == 4) {
					try {
						refresh();
						DecksPanel.this.setSelectedIndex(0);
					} catch (IOException | ResponseRequestException | RequestException | DeckException e1) {
						JOptionPane.showMessageDialog(DecksPanel.this, e1.getMessage(), "Error Refresh", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	public void refresh() throws IOException, ResponseRequestException, RequestException, DeckException {
		decks.refresh();
		this.dark.setDeck(decks.getDeckDark());
		this.light.setDeck(decks.getDeckLight());
		this.nature.setDeck(decks.getDeckNature());
		this.toxic.setDeck(decks.getDeckToxic());
		this.repaint();
	}
	
	public void update() throws IOException, ResponseRequestException, RequestException {
		decks.update();
	}
	
}
