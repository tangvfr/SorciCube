package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import fr.tangv.sorcicubespell.cards.Card;
import fr.tangv.sorcicubespell.cards.CardCible;
import fr.tangv.sorcicubespell.cards.CardFaction;
import fr.tangv.sorcicubespell.cards.CardFeature;
import fr.tangv.sorcicubespell.cards.CardFeatureType;
import fr.tangv.sorcicubespell.cards.CardFeatures;
import fr.tangv.sorcicubespell.cards.CardRarity;
import fr.tangv.sorcicubespell.cards.CardType;
import fr.tangv.sorcicubespell.cards.CardValue;
import fr.tangv.sorcicubespell.cards.CardMaterial;

public class PanelNav extends JPanel {

	private static final long serialVersionUID = -493103431246777186L;
	private CardsPanel cartsPanel;
	private JButton refrech;
	private JButton disconnect;
	private JList<Card> list;
	
	public PanelNav(CardsPanel cartsPanel) {
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
				cartsPanel.getFrameLogi().showConnection();
			}
		});
		disconnect.setFocusable(false);
		//list
		this.list = new JList<Card>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setComponentPopupMenu(new ListPopupMenu(this.cartsPanel));
		list.setCellRenderer(new ListCellRenderer<Card>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Card> list, Card cart, int index, boolean isSelected, boolean cellHasFocus) {
				String prefix = (isSelected ? ">" : "") + (cart.getType() == CardType.ENTITY ? "[E]" : "[S]");
				return new JLabel("<html><body><span>"+prefix+"</span>"+ColorMCToHTML.replaceColor(cart.getName())+"</body></html>");
			}
		});
		list.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Card cart = list.getSelectedValue();
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
		this.list.setListData(new Vector<Card>(this.cartsPanel.getCarts().getCarts()));
	}
	
	private class ListPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 777350512568935632L;
		
		public ListPopupMenu(CardsPanel cartsPanel) {
			JMenuItem itemCreateSort = new JMenuItem("Create Card Sort");
			itemCreateSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						Card cart = list.getSelectedValue();
						cartsPanel.getCarts().insert(new Card(
								UUID.randomUUID(), 
								new CardMaterial(1, 0),
								"§4NoNameSort",
								CardType.SORT,
								CardRarity.COMMUN,
								CardFaction.BASIC,
								CardCible.ONE_ENTITY_ENEMIE,
								CardFaction.BASIC,
								1,
								new CardFeatures(),
								new ArrayList<String>())
							);
						refrech();
						list.setSelectedValue(cart, true);
					}
				}
			});
			this.add(itemCreateSort);
			JMenuItem itemCreateEntity = new JMenuItem("Create Card Entity");
			itemCreateEntity.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						Card cart = list.getSelectedValue();
						CardFeatures features = new CardFeatures();
						features.putFeature(new CardFeature("Health", CardFeatureType.HEALTH, new CardValue(3)));
						features.putFeature(new CardFeature("AttackDamage", CardFeatureType.DAMAGE, new CardValue(1)));
						cartsPanel.getCarts().insert(new Card(
								UUID.randomUUID(), 
								new CardMaterial(3, 0),
								"§4NoNameEntity",
								CardType.ENTITY,
								CardRarity.COMMUN,
								CardFaction.BASIC,
								CardCible.ONE_ENTITY_ENEMIE,
								CardFaction.BASIC,
								1,
								features,
								new ArrayList<String>()
							));
						refrech();
						list.setSelectedValue(cart, true);
					}
				}
			});
			this.add(itemCreateEntity);
			JMenuItem itemRemove = new JMenuItem("Remove Card");
			itemRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						Card cart = list.getSelectedValue();
						if (cart != null) {
							if (0 == JOptionPane.showConfirmDialog(PanelNav.this, "Are you sure delete this Card ?", "Delete card", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
								cartsPanel.getCarts().delete(cart);
								refrech();
								cartsPanel.getTable().setModel(new DefaultTableModel());
							}
						} else {
							JOptionPane.showMessageDialog(PanelNav.this, "No selected card ?", "Delete Card", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			this.add(itemRemove);
		}
		
	}
	
}
