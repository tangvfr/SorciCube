package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

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
import fr.tangv.sorcicubespell.card.CardVisual;
import fr.tangv.sorcicubespell.logi.dialog.DialogCombo;

public class PanelNav extends JPanel {

	private static final long serialVersionUID = -493103431246777186L;
	private CardsPanel cartsPanel;
	private JButton refrech;
	private JLabel clear;
	private JButton disconnect;
	private JTextField search;
	private JList<Card> list;
	private Vector<Card> listValue;
	private PanelFilter filter;
	private JCheckBox filterApply;
	private CardComparator sort;
	
	public PanelNav(CardsPanel cartsPanel) throws Exception {
		this.cartsPanel = cartsPanel;
		//clear
		this.clear = new JLabel(" X ");
		clear.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search.setText("");
				cartsPanel.refresh();
			}
		});
		clear.setForeground(Color.RED);
		//filter
		this.filter = new PanelFilter();
		filter.setVisible(false);
		this.filterApply = new JCheckBox("Filter", false);
		this.filterApply.addActionListener((ActionEvent e) -> {
			if (e.getID() == 1001) {
				filter.setVisible(filterApply.isSelected());
				PanelNav.this.getParent().repaint();
			}
		});
		//info
		this.search = new JTextField();
		search.setToolTipText("name of card or uuid of card");
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cartsPanel.refresh();
				}
			}
		});
		//refrech
		this.refrech = new JButton("Refrech");
		refrech.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cartsPanel.refresh();
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
				return new JLabel(renderHTMLCard(card, (isSelected ? ">" : "")));
			}
		});
		list.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cartsPanel.setCard(list.getSelectedValue());
				
			}
		});
		//Display
		this.setLayout(new BorderLayout());
		JPanel panelUp = new JPanel(new BorderLayout());
		panelUp.setLayout(new BorderLayout());
		panelUp.add(this.refrech, BorderLayout.NORTH);
		panelUp.add(this.filter, BorderLayout.CENTER);
		JPanel searchBar = new JPanel();
		searchBar.setLayout(new BorderLayout());
		searchBar.add(this.clear, BorderLayout.WEST);
		searchBar.add(this.search, BorderLayout.CENTER);
		searchBar.add(this.filterApply, BorderLayout.EAST);
		panelUp.add(searchBar, BorderLayout.SOUTH);
		this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelUp, new JScrollPane(this.list)), BorderLayout.CENTER);
		this.add(this.disconnect, BorderLayout.SOUTH);
		this.sort = CardComparator.BY_ID;
	}
	
	public static String renderHTMLCard(Card card, String prefix) {
		if (card.getMaterial().isInvalid())
			prefix += "<span color=\"#0652E9\">[Material*]</span>";
		if (card.getFeatures().hasFeature(CardFeatureType.HIDE_CARD))
			prefix += "<span color=\"#E60FB8\">[Hide]</span>";
		prefix += (card.getType() == CardType.ENTITY ? 
				"<span color=\"#E8A006\">[Entity]</span>" 
				: "<span color=\"#E64D0F\">[Spell]</span>");
		if (card.getFeatures().hasFeature(CardFeatureType.SKIN))
			prefix += "<span color=\"#2BBFE0\">[Skin]</span>";
		prefix += "<span color=\"#000000\"> | </span>";
		return "<html><body><span>"+prefix+"</span>"+
				ColorMCToHTML.replaceColor(card.renderName())+
				"<span color=\"#000000\"> | </span>"+
				ColorMCToHTML.replaceColor(CardVisual.renderManaCard(card))+
				"<span color=\"#000000\"> | </span>"+
				ColorMCToHTML.replaceColor(CardVisual.renderStatCard(card))+
				"</body></html>";
	}
	
	public void refresh() {
		Vector<Card> listCard = this.cartsPanel.getCards().cloneCardsValue();
		String name = this.search.getText().toLowerCase();
		boolean uuidSearch = false;
		if (!name.isEmpty()) {
			listValue = new Vector<Card>();
			for (Card card : listCard)
				if (card.getName().toLowerCase().contains(name)) {
					listValue.add(card);
				} else if (card.getUUID().toString().equalsIgnoreCase(name)) {
					listValue.add(card);
					uuidSearch = true;
				}
		} else {
			listValue = listCard;
		}
		if (!uuidSearch && this.filterApply.isSelected())
			listValue = this.filter.applyFilter(listValue);
		//display
		this.refrech.setText("Refrech | "+Integer.toString(listCard.size())+" cards "+Integer.toString(listValue.size())+" find");
		listValue.sort(CardComparator.BY_ID);
		listValue.sort(sort);
		this.list.setListData(listValue);
	}
	
	public void setCardSelectedInList(Card card) {
		this.list.setSelectedIndex(listValue.indexOf(card));
	}
	
	private class ListPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 777350512568935632L;
		
		public ListPopupMenu(CardsPanel cardsPanel) {
			JMenuItem itemCreateSort = new JMenuItem("Create Card Spell");
			itemCreateSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						CardFeatures features = new CardFeatures();
						features.putFeature(new CardFeature(CardFeatureType.HIDE_CARD, new CardValue()));
						Card card = new Card(
								UUID.randomUUID(), 
								new CardMaterial(1, 0),
								"§4NoNameSpell",
								CardType.SPELL,
								CardRarity.COMMUN,
								CardFaction.BASIC,
								CardCible.ONE_ENEMIE,
								CardFaction.BASIC,
								1,
								features,
								new ArrayList<String>(),
								false
							);
						cardsPanel.getCards().insert(card);
						cardsPanel.setCard(card);
						cardsPanel.refresh();
					}
				}
			});
			this.add(itemCreateSort);
			JMenuItem itemCreateEntity = new JMenuItem("Create Card Entity");
			itemCreateEntity.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						CardFeatures features = new CardFeatures();
						features.putFeature(new CardFeature(CardFeatureType.HEALTH, new CardValue(3)));
						features.putFeature(new CardFeature(CardFeatureType.DAMAGE, new CardValue(1)));
						features.putFeature(new CardFeature(CardFeatureType.HIDE_CARD, new CardValue()));
						Card card = new Card(
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
								new ArrayList<String>(),
								false
							);
						cardsPanel.getCards().insert(card);
						cardsPanel.setCard(card);
						cardsPanel.refresh();
					}
				}
			});
			this.add(itemCreateEntity);
			JMenuItem itemRemove = new JMenuItem("Remove Card");
			itemRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getID() == 1001) {
						Card card = list.getSelectedValue();
						if (card != null) {
							if (0 == JOptionPane.showConfirmDialog(PanelNav.this, "Are you sure delete card nommed \""+card.getName()+"\" ?", "Delete card", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
								cardsPanel.getCards().delete(card);
								cardsPanel.setCard(null);
								cardsPanel.refresh();
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
						new DialogCombo<CardComparator>(cardsPanel.getFrameLogi(), "Sorted by", sort) {
							private static final long serialVersionUID = 944290591647698175L;
									
							@Override
							public void eventOk(CardComparator newSort) {
								sort = newSort;
								cardsPanel.refresh();
							}
						}; 
					}
				}
			});
			this.add(itemSort);
		}
		
	}
	
}
