package fr.tangv.sorcicubespell.logi;

import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubespell.carts.Cart;

public class ModelEditCart extends AbstractTableModel {

	private static final long serialVersionUID = -3498539069595000083L;
	private Cart cart;
	
	public ModelEditCart(Cart cart) {
		super();
		this.cart = cart;
	}
	
	public Cart getCart() {
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

	public static final String[] nameRow = {"id", "name", "type", "material", "rarity", "faction", "cible", "cible_faction", "mana", "features"};
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return nameRow[rowIndex];
		} else {
			switch (rowIndex) {
				case 0:
					return cart.getUUID();
					
				case 1:
					return cart.getName();
					
				case 2:
					return cart.getType();
					
				case 3:
					return cart.getMaterial();
					
				case 4:
					return cart.getRarity();
					
				case 5:
					return cart.getFaction();
					
				case 6:
					return cart.getCible();
					
				case 7:
					return cart.getCibleFaction();
					
				case 8:
					return cart.getMana();
					
				case 9:
					return cart.getFeatures().size();
						
				default:
					return null; //not possible
			}
			/*private UUID id
			private String name;
			private CartType type;
			private CartMaterial material;
			
			private CartRarity rarity;
			private CartFaction faction;
			private CartCible cible;
			private CartFaction cibleFaction;
			
			private int mana;
			private CartFeatures features;*/
		}
	}
	
}
