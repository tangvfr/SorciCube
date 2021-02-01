package fr.tangv.sorcicubeapp.tabbed;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.UUIDPanelEditor;
import fr.tangv.sorcicubeapp.dialog.DialogBase;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardValue;
import fr.tangv.sorcicubecore.player.DeckCards;

public class DeckPanel extends JList<Card> {

	private static final long serialVersionUID = 1426081281440355960L;
	private DeckCards deck;
	
	public DeckPanel(CardsPanel cardsPanel) {
		this.setCellRenderer(cardsPanel.getPanelNav().getListCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Card card = DeckPanel.this.getSelectedValue();
				new DialogBase<UUIDPanelEditor>(cardsPanel.getFrameLogi(), "Value", new UUIDPanelEditor(cardsPanel, (card != null ? card.getUUID() : CardValue.EMPTY_UUID))) {
					private static final long serialVersionUID = 4116920655857733838L;

					@Override
					public void eventOk(UUIDPanelEditor comp) {
						try {
							deck.setCard(
									DeckPanel.this.getSelectedIndex(),
									cardsPanel.getCards().getCard(comp.getCardUUID())
								);
							DeckPanel.this.repaint();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(this, e.getMessage(), "Error invalid UUID", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					@Override
					protected void initComp(UUIDPanelEditor comp) {
						comp.setParent(this);
					}
				};
			}
		});
	}
	
	public void setDeck(DeckCards deck) {
		this.deck = deck;
		this.setListData(deck.getCards());
	}
	
}
