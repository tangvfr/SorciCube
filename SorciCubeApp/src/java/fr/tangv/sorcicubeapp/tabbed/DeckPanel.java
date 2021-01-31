package fr.tangv.sorcicubeapp.tabbed;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.UUIDPanelEditor;
import fr.tangv.sorcicubeapp.dialog.DialogBase;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.card.Card;

public class DeckPanel extends JList<Card> {

	private static final long serialVersionUID = 1426081281440355960L;

	public DeckPanel(CardsPanel cardsPanel) {
		this.setCellRenderer(cardsPanel.getPanelNav().getListCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Card card = DeckPanel.this.getSelectedValue();
				if (card != null)
					new DialogBase<UUIDPanelEditor>(cardsPanel.getFrameLogi(), "Value", new UUIDPanelEditor(cardsPanel, card.getUUID())) {
						private static final long serialVersionUID = 4116920655857733839L;
	
						@Override
						public void eventOk(UUIDPanelEditor comp) {
							
						}
						
						@Override
						protected void initComp(UUIDPanelEditor comp) {
							comp.setParent(this);
						}
					};
			}
		});
	}
	
	
}
