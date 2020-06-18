package fr.tangv.sorcicubespell.logi;

import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubespell.cards.Card;

public class ModelEditCart extends AbstractTableModel {

	private static final long serialVersionUID = -3498539069595000083L;
	private Card cart;
	
	public ModelEditCart(Card cart) {
		super();
		this.cart = cart;
	}
	
	public Card getCart() {
		return cart;
	}

	@Override
	public int getRowCount() {
		return 10;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	public static final String[] nameRow = {"ID", "Name", "Type", "Material", "Rarity", "Faction", "Cible", "Cible Faction", "Mana", "Features"};
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String text;
		if (columnIndex == 0) {
			text = nameRow[rowIndex];
		} else {
			switch (rowIndex) {
				case 0:
					text = cart.getUUID().toString();
					break;
					
				case 1:
					text = cart.getName();
					break;
					
				case 2:
					text = cart.getType().name();
					break;
					
				case 3:
					text = cart.getMaterial().toString();
					break;
					
				case 4:
					text = cart.getRarity().name();
					break;
					
				case 5:
					text = cart.getFaction().name();
					break;
					
				case 6:
					text = cart.getCible().name();
					break;
					
				case 7:
					text = cart.getCibleFaction().name();
					break;
					
				case 8:
					text = ""+cart.getMana();
					break;
					
				case 9:
					text = ""+cart.getFeatures().size();
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
