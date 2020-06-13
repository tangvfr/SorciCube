package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartFaction;
import fr.tangv.sorcicubespell.carts.CartFeature;
import fr.tangv.sorcicubespell.carts.CartFeatureType;
import fr.tangv.sorcicubespell.carts.CartFeatures;
import fr.tangv.sorcicubespell.carts.CartMaterial;
import fr.tangv.sorcicubespell.carts.CartRarity;
import fr.tangv.sorcicubespell.carts.CartType;
import fr.tangv.sorcicubespell.carts.CartValue;

public class PanelNav extends JPanel {

	private static final long serialVersionUID = -493103431246777186L;
	private CartsPanel cartsPanel;
	private JButton refrech;
	private JButton disconnect;
	private JList<Cart> list;
	
	public PanelNav(CartsPanel cartsPanel) {
		this.cartsPanel = cartsPanel;
		//refrech
		this.refrech = new JButton("Refrech");
		refrech.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cartsPanel.refrech();
			}
		});
		refrech.setFocusable(false);
		//disconnect
		this.disconnect = new JButton("Disconnect");
		disconnect.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cartsPanel.getFl().showConnection();
			}
		});
		disconnect.setFocusable(false);
		//list
		this.list = new JList<Cart>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setComponentPopupMenu(new ListPopupMenu(this.cartsPanel));
		list.setCellRenderer(new ListCellRenderer<Cart>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Cart> list, Cart cart, int index, boolean isSelected, boolean cellHasFocus) {
				String prefix = (isSelected ? ">" : "");
				return new JLabel("<html><body><span>"+prefix+"</span>"+ColorMCToHTML.replaceColor(cart.getName())+"</body></html>");
			}
		});
		list.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cart cart = list.getSelectedValue();
				if (cart != null) {
					cartsPanel.getTable().setModel(new ModelEditCart(cart));
				} else {
					cartsPanel.getTable().setModel(new DefaultTableModel());
				}
			}
		});
		//PanelNav
		this.setLayout(new BorderLayout());
		this.add(this.refrech, BorderLayout.NORTH);
		this.add(this.list, BorderLayout.CENTER);
		this.add(this.disconnect, BorderLayout.SOUTH);
		this.refrech();
	}
	
	public void refrech() {
		this.list.setListData(new Vector<Cart>(this.cartsPanel.getCarts().getCarts()));
	}
	
	private class ListPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 777350512568935632L;
		
		public ListPopupMenu(CartsPanel cartsPanel) {
			JMenuItem itemCreateSort = new JMenuItem("Create Carts Sort");
			itemCreateSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						cartsPanel.getCarts().insert(new Cart(
								UUID.randomUUID(), 
								new CartMaterial(1, 0),
								"§4NoNameSort",
								CartType.SORT,
								CartRarity.COMMUN,
								CartFaction.BASIC,
								CartCible.ONE_ENTITY_ENEMIE,
								CartFaction.BASIC,
								1,
								new CartFeatures())
							);
						refrech();
						System.out.println("Insert new sort");
					}
				}
			});
			this.add(itemCreateSort);
			JMenuItem itemCreateEntity = new JMenuItem("Create Carts Entity");
			itemCreateEntity.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						CartFeatures features = new CartFeatures();
						features.putFeature(new CartFeature("Health", CartFeatureType.HEALTH, new CartValue(3)));
						features.putFeature(new CartFeature("AttackDamage", CartFeatureType.DAMAGE, new CartValue(1)));
						cartsPanel.getCarts().insert(new Cart(
								UUID.randomUUID(), 
								new CartMaterial(3, 0),
								"§4NoNameEntity",
								CartType.ENTITY,
								CartRarity.COMMUN,
								CartFaction.BASIC,
								CartCible.ONE_ENTITY_ENEMIE,
								CartFaction.BASIC,
								1,
								features
							));
						refrech();
						System.out.println("Insert new entity");
					}
				}
			});
			this.add(itemCreateEntity);
		}
		
	}
	
}
