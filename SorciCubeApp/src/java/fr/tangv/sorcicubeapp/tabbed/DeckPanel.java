package fr.tangv.sorcicubeapp.tabbed;

import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.UUIDPanelEditor;
import fr.tangv.sorcicubeapp.dialog.DialogBase;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.card.Card;
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
				if (e.getButton() == MouseEvent.BUTTON1) {
					Card card = DeckPanel.this.getSelectedValue();
					if (card != null)
						new DialogBase<UUIDPanelEditor>(cardsPanel.getFrameLogi(), "Value", new UUIDPanelEditor(cardsPanel, card.getUUID())) {
							private static final long serialVersionUID = 4116920655857733838L;

							@Override
							public void eventOk(UUIDPanelEditor comp) {
								cardsPanel.getFrameLogi().setVisible(true);
							}
							
							@Override
							protected void initComp(UUIDPanelEditor comp) {
								comp.setParent(this);
							}
						};
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					String input = JOptionPane.showInputDialog(DeckPanel.this, "enter text", "Test", JOptionPane.INFORMATION_MESSAGE);
					Card card = null;
					try {
						card = cardsPanel.getCards().getCard(UUID.fromString(input));
					} catch (IllegalArgumentException e1) {}
					deck.setCard(DeckPanel.this.getSelectedIndex(), card);
					DeckPanel.this.repaint();
				}
			}
		});
	}
	
	public void setDeck(DeckCards deck) {
		this.deck = deck;
		this.setListData(deck.getCards());
	}
	
}
