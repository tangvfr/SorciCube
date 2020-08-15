package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardComparator;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardFeatures;
import fr.tangv.sorcicubespell.card.CardMaterial;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.card.CardValue;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;

public class PanelNav extends JPanel {

	private static final long serialVersionUID = -493103431246777186L;
	private CardsPanel cartsPanel;
	private JButton refrech;
	private JButton disconnect;
	private JTextField search;
	private JList<Card> list;
	private CardComparator sort;
	
	public PanelNav(CardsPanel cartsPanel) {
		this.cartsPanel = cartsPanel;
		//info
		this.search = new JTextField();
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cartsPanel.refrech();
				}
			}
		});
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
			public Component getListCellRendererComponent(JList<? extends Card> list, Card card, int index, boolean isSelected, boolean cellHasFocus) {
				String prefix = (isSelected ? ">" : "");
				/*if (warning)
					prefix += "<span color=\"#d61818\" style=\"text-decoration: underline;\">[/!\\]</span>";*/
				if (card.getFeatures().hasFeature(CardFeatureType.HIDE_CART))
					prefix += "<span color=\"#E60FB8\">[Hide]</span>";
				prefix += (card.getType() == CardType.ENTITY ? 
						"<span color=\"#E8A006\">[Entity]</span>" 
						: "<span color=\"#E64D0F\">[Spell]</span>");
				if (card.getFeatures().hasFeature(CardFeatureType.SKIN))
					prefix += "<span color=\"#2BBFE0\">[Skin]</span>";
				prefix += "<span color=\"#000000\"> | </span>";
				return new JLabel("<html><body><span>"+prefix+"</span>"+ColorMCToHTML.replaceColor(card.getName())+"</body></html>");
			}
		});
		list.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Card cart = list.getSelectedValue();
				if (cart != null) {
					cartsPanel.getTable().setModel(new ModelEditCard(cart));
				} else {
					cartsPanel.getTable().setModel(new DefaultTableModel());
				}
			}
		});
		//PanelNav
		this.setLayout(new BorderLayout());
		JPanel panelUp = new JPanel(new BorderLayout());
		panelUp.add(this.refrech, BorderLayout.NORTH);
		panelUp.add(this.search, BorderLayout.SOUTH);
		this.add(panelUp, BorderLayout.NORTH);
		this.add(new JScrollPane(this.list), BorderLayout.CENTER);
		this.add(this.disconnect, BorderLayout.SOUTH);
		this.sort = CardComparator.BY_ID;
		this.refresh();
	}
	
	public void refresh() {
		this.cartsPanel.getCarts().refresh();
		Vector<Card> listCard = this.cartsPanel.getCarts().cloneCardsValue();
		Vector<Card> list;
		String name = this.search.getText().toLowerCase();
		if (!name.isEmpty()) {
			list = new Vector<Card>();
			for (Card card : listCard)
				if (card.getName().toLowerCase().contains(name) || card.getUUID().toString().equalsIgnoreCase(name)) {
					list.add(card);
				}
		} else {
			list = listCard;
		}
		this.refrech.setText("Refrech | "+Integer.toString(listCard.size())+" cards "+Integer.toString(list.size())+" find");
		list.sort(CardComparator.BY_ID);
		list.sort(sort);
		this.list.setListData(list);
		if (cartsPanel.getTable().getModel() instanceof ModelEditCard) {
			Card card = ((ModelEditCard) cartsPanel.getTable().getModel()).getCard();
			this.list.setSelectedIndex(list.indexOf(card));
		}
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
						CardFeatures features = new CardFeatures();
						features.putFeature(new CardFeature(CardFeatureType.HIDE_CART, new CardValue()));
						cartsPanel.getCarts().insert(new Card(
								UUID.randomUUID(), 
								new CardMaterial(1, 0),
								"§4NoNameSort",
								CardType.SORT,
								CardRarity.COMMUN,
								CardFaction.BASIC,
								CardCible.ONE_ENEMIE,
								CardFaction.BASIC,
								1,
								features,
								new ArrayList<String>())
							);
						refresh();
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
						Card card = list.getSelectedValue();
						CardFeatures features = new CardFeatures();
						features.putFeature(new CardFeature(CardFeatureType.HEALTH, new CardValue(3)));
						features.putFeature(new CardFeature(CardFeatureType.DAMAGE, new CardValue(1)));
						features.putFeature(new CardFeature(CardFeatureType.HIDE_CART, new CardValue()));
						cartsPanel.getCarts().insert(new Card(
								UUID.randomUUID(), 
								new CardMaterial(3, 0),
								"§4NoNameEntity",
								CardType.ENTITY,
								CardRarity.COMMUN,
								CardFaction.BASIC,
								CardCible.ONE_ENEMIE,
								CardFaction.BASIC,
								1,
								features,
								new ArrayList<String>()
							));
						refresh();
						list.setSelectedValue(card, true);
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
								refresh();
								cartsPanel.getTable().setModel(new DefaultTableModel());
							}
						} else {
							JOptionPane.showMessageDialog(PanelNav.this, "No selected card ?", "Delete Card", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			this.add(itemRemove);
			JMenuItem itemSort = new JMenuItem("Change Sort");
			itemSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						new DialogCombo<CardComparator>(cartsPanel.getFrameLogi(), "Sorted by", sort) {
							private static final long serialVersionUID = 944290591647698175L;
									
							@Override
							public void eventOk(CardComparator newSort) {
								sort = newSort;
								refresh();
							}
						}; 
					}
				}
			});
			this.add(itemSort);
		}
		
	}
	
}
