package fr.tangv.sorcicubespell.logi;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartFaction;
import fr.tangv.sorcicubespell.carts.CartMaterial;
import fr.tangv.sorcicubespell.carts.CartRarity;
import fr.tangv.sorcicubespell.logi.dialog.DialogBase;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;

public class MouseTable extends ClickListener {

	private JTable table;
	private CartsPanel cartsPanel;
	
	public MouseTable(JTable table, CartsPanel cartsPanel) {
		this.table = table;
		this.cartsPanel = cartsPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.getSelectedColumn();
		int row = table.getSelectedRow();
		if (table.getModel() instanceof ModelEditCart && col == 1) {
			ModelEditCart edit = (ModelEditCart) table.getModel();
			Cart cart = edit.getCart();
			if (row == 0 || row == 2)
				return;
			switch (row) {
				case 1:
					new DialogBase<JTextField>(cartsPanel.getFrameLogi(), "Name", new JTextField(cart.getName())) {
						private static final long serialVersionUID = -4613034932047272120L;

						@Override
						public void eventOk(JTextField comp) {
							cart.setName(comp.getText());
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;
					
				case 3:
					new DialogBase<JTextField>(cartsPanel.getFrameLogi(), "Material", new JTextField(cart.getMaterial().toString())) {
						private static final long serialVersionUID = -4613034932047272120L;

						@Override
						public void eventOk(JTextField comp) {
							CartMaterial material = null;
							String text = comp.getText();
							if (text.startsWith("skull: ")) {
								material = new CartMaterial(text.replaceFirst("skull: ", ""));
							} else {
								String[] split = text.split(":");
								if (split.length == 2 || split.length == 1) {
									try {
										int id = Integer.parseInt(split[0]);
										int data = 0;
										if (split.length == 2)
											data = Integer.parseInt(split[1]);
										material = new CartMaterial(id, data);
									} catch (Exception e) {}
								}
							}
							if (material != null) {
								cart.setMaterial(material);
								cartsPanel.getCarts().update(cart);
								cartsPanel.refrech();
							} else {
								JOptionPane.showMessageDialog(this, "Error invalid Material", "Error Cart Material", JOptionPane.ERROR_MESSAGE);
							}
						}
					};
					break;
					
				case 4:
					new DialogCombo<CartRarity>(cartsPanel.getFrameLogi(), "Rarity", cart.getRarity()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CartRarity enumCombo) {
							cart.setRarity(enumCombo);
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;
					
				case 5:
					new DialogCombo<CartFaction>(cartsPanel.getFrameLogi(), "Faction", cart.getFaction()) {
						private static final long serialVersionUID = -4807395720412058130L;

						@Override
						public void eventOk(CartFaction enumCombo) {
							cart.setFaction(enumCombo);
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;

				case 6:
					new DialogCombo<CartCible>(cartsPanel.getFrameLogi(), "Cible", cart.getCible()) {
						private static final long serialVersionUID = -4807395720412058131L;

						@Override
						public void eventOk(CartCible enumCombo) {
							cart.setCible(enumCombo);
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;
					
				case 7:
					new DialogCombo<CartFaction>(cartsPanel.getFrameLogi(), "Cible Faction", cart.getCibleFaction()) {
						private static final long serialVersionUID = -4807395720412058129L;

						@Override
						public void eventOk(CartFaction enumCombo) {
							cart.setCibleFaction(enumCombo);
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;
				
				case 8:
					new DialogBase<JSpinner>(cartsPanel.getFrameLogi(), "Mana", new JSpinner(new SpinnerNumberModel(cart.getMana(), 0, Integer.MAX_VALUE, 1))) {
						private static final long serialVersionUID = -4613034932048272120L;

						@Override
						public void eventOk(JSpinner comp) {
							cart.setMana((int) comp.getValue());
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};
					break;
					
				case 9:
					/*new DialogBase<JTable>(cartsPanel.getFrameLogi(), "Features", )) {
						private static final long serialVersionUID = -4613024932048272120L;

						@Override
						public void eventOk(JTable comp) {
							cart.se
							cartsPanel.getCarts().update(cart);
							cartsPanel.refrech();
						}
					};*/
					break;
						
				default:
					Object ob = edit.getValueAt(row, col);
					if (ob != null)
						JOptionPane.showMessageDialog(cartsPanel, "name: "+ob.getClass().getSimpleName()+"\nvalue: "+ob.toString(), "Feature Cart No Editable", JOptionPane.INFORMATION_MESSAGE);
					break;
			}
		}
	}
	
}
