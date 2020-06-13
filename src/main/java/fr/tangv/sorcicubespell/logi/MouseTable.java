package fr.tangv.sorcicubespell.logi;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class MouseTable extends ClickListener {

	private JTable table;
	private CartsPanel cartsPanel;
	
	public MouseTable(JTable table, CartsPanel cartsPanel) {
		this.table = table;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.getSelectedColumn();
		int row = table.getSelectedRow();
		if (table.getModel() instanceof ModelEditCart && col == 1) {
			ModelEditCart edit = (ModelEditCart) table.getModel();
			//Cart cart = edit.getCart();
			Object ob = edit.getValueAt(row, col);
			if (ob != null)
				JOptionPane.showMessageDialog(cartsPanel, "name: "+ob.getClass().getSimpleName()+"\nvalue: "+ob.toString());
			/*switch (row) {
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
			}*/
		}
	}
	
}
