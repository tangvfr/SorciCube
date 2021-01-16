package fr.tangv.sorcicubeapp;

import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.util.TextList;

public class ModelEditCard extends AbstractTableModel {

	private static final long serialVersionUID = -3498539069595000083L;
	private Card card;
	
	public ModelEditCard(Card cart) {
		super();
		this.card = cart;
	}
	
	public Card getCard() {
		return card;
	}

	@Override
	public int getRowCount() {
		return 12;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	public static final String[] nameRow = {"ID", "Name", "Type", "Material", "Rarity", "Faction", "Cible", "Cible Faction", "Mana", "Features", "Description", "Orignal Name"};
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String text;
		if (columnIndex == 0) {
			text = nameRow[rowIndex];
		} else {
			switch (rowIndex) {
				case 0:
					text = card.getUUID().toString();
					break;
					
				case 1:
					text = card.getName();
					break;
					
				case 2:
					text = card.getType().name();
					break;
					
				case 3:
					text = card.getMaterial().toString();
					break;
					
				case 4:
					text = card.getRarity().name();
					break;
					
				case 5:
					text = card.getFaction().name();
					break;
					
				case 6:
					text = card.getCible().name();
					break;
					
				case 7:
					text = card.getCibleFaction().name();
					break;
					
				case 8:
					text = ""+card.getMana();
					break;
					
				case 9:
					text = ""+card.getFeatures().size();
					break;

				case 10:
					text = TextList.listToText(card.getDescription());
					break;
					
				case 11:
					text = ""+card.isOriginalName();
					break;
						
				default:
					text = null; //not possible
			}
		}
		if (rowIndex == 0 || rowIndex == 2)
			return "<html><body><span color='#5555FF'>"+text+"</span></body></html>";
		else
			return text;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
}
