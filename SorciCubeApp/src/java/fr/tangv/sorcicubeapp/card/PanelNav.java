package fr.tangv.sorcicubeapp.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
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

import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.dialog.DialogCombo;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeature;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardFeatures;
import fr.tangv.sorcicubecore.card.CardMaterial;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue;
import fr.tangv.sorcicubecore.card.CardVisual;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;

public class PanelNav extends JPanel {

	private static final long serialVersionUID = -493103431246777186L;
	private final ListCellRenderer<Card> listCellRenderer;
	private final JLabel sortedBy;
	private final JSplitPane splitPane;
	private final CardsPanel cardsPanel;
	private final HandlerCards handler;
	private final JButton refrech;
	private final JLabel clear;
	private final JTextField search;
	private final JList<Card> list;
	private final PanelFilter filter;
	private final JCheckBox filterApply;
	private Vector<Card> listValue;
	private CardComparator sort;
	
	public PanelNav(CardsPanel cardsPanel) throws PanelFilterException {
		this.cardsPanel = cardsPanel;
		this.handler = this.cardsPanel.getCards();
		//clear
		this.clear = new JLabel(" X ");
		clear.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search.setText("");
				PanelNav.this.refrechCardPanel();
			}
		});
		clear.setForeground(Color.RED);
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
					PanelNav.this.refrechCardPanel();
				}
			}
		});
		//refrech
		this.refrech = new JButton("Refrech");
		refrech.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PanelNav.this.refrechCardPanel();
			}
		});
		refrech.setFocusable(false);
		//list
		this.list = new JList<Card>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setComponentPopupMenu(new ListPopupMenu(this.cardsPanel));
		this.listCellRenderer = new ListCellRenderer<Card>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Card> list, Card card, int index, boolean isSelected, boolean cellHasFocus) {
				if (card != null)
					return new JLabel(renderHTMLCard(card, (isSelected ? ">" : "")));
				else
					return new JLabel((isSelected ? ">" : "")+"*None*");
			}
		};
		list.setCellRenderer(this.listCellRenderer);
		list.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardsPanel.setCard(list.getSelectedValue());
			}
		});
		//filter
		this.filter = new PanelFilter();
		filter.setVisible(false);
		this.filterApply = new JCheckBox("Filter", false);
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
		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelUp, new JScrollPane(this.list));
		
		//filterApply
		this.filterApply.addActionListener((ActionEvent e) -> {
			if (e.getID() == 1001) {
				if (filterApply.isSelected()) {
					filter.setVisible(true);
					splitPane.setDividerLocation(0.4);
				} else {
					filter.setVisible(false);
					splitPane.setDividerLocation(splitPane.getMinimumDividerLocation());
				}
				PanelNav.this.getParent().repaint();
			}
		});
		
		//finish Display
		this.add(this.splitPane, BorderLayout.CENTER);
		this.sort = CardComparator.BY_ID;
		this.sortedBy = new JLabel("Sorted by ???");
		this.sortedBy.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changeSort();
			}
		});
		sortedBy.setHorizontalAlignment(JLabel.CENTER);
		updateTextSortedBy();
		this.add(sortedBy, BorderLayout.SOUTH);
	}
	
	public ListCellRenderer<Card> getListCellRenderer() {
		return listCellRenderer;
	}

	private void updateTextSortedBy() {
		this.sortedBy.setText("Sorted "+this.sort.name());
	}
	
	private void changeSort() {
		new DialogCombo<CardComparator>(cardsPanel.getFrameLogi().getFramePanel(), "Change", "Sorted by", sort) {
			private static final long serialVersionUID = 944290591647698175L;
					
			@Override
			public void eventOk(CardComparator newSort) throws IOException, ResponseRequestException, RequestException  {
				sort = newSort;
				updateTextSortedBy();
				cardsPanel.refresh();
			}
			
		};
	}
	
	public static String renderHTMLCard(Card card, String prefix) {
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
		Vector<Card> listCard = handler.cloneValues();
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
			listValue = this.filter.applyFilter(listValue, handler);
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
						try {
							cardsPanel.getCards().put(card);
							cardsPanel.setCard(card);
							cardsPanel.refresh();
						} catch (IOException | ResponseRequestException | RequestException e1) {
							JOptionPane.showMessageDialog(PanelNav.this, "Error: "+e1.getMessage(), "Create Card Spell", JOptionPane.ERROR_MESSAGE);
						}
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
						try {
							cardsPanel.getCards().put(card);
							cardsPanel.setCard(card);
							cardsPanel.refresh();
						} catch (IOException | ResponseRequestException | RequestException e1) {
							JOptionPane.showMessageDialog(PanelNav.this, "Error: "+e1.getMessage(), "Create Card Entity", JOptionPane.ERROR_MESSAGE);
						}
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
							if (0 == JOptionPane.showConfirmDialog(PanelNav.this, "Are you sure remove card nommed \""+card.getName()+"\" ?", "Remove card", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
								try {
									cardsPanel.getCards().remove(card);
									cardsPanel.setCard(null);
									cardsPanel.refresh();
								} catch (IOException | ResponseRequestException | RequestException e1) {
									JOptionPane.showMessageDialog(PanelNav.this, "Error: "+e1.getMessage(), "Remove Card", JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							JOptionPane.showMessageDialog(PanelNav.this, "No selected card ?", "Remove Card", JOptionPane.ERROR_MESSAGE);
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
						changeSort();
					}
				}
			});
			this.add(itemSort);
		}
		
	}
	
	private void refrechCardPanel() {
		try {
			cardsPanel.refresh();
		} catch (IOException | ResponseRequestException | RequestException e) {
			JOptionPane.showMessageDialog(PanelNav.this, "Error: "+e.getMessage(), "Error refrech", JOptionPane.ERROR_MESSAGE);
			cardsPanel.getFrameLogi().showConnection("Error: "+e.getMessage(), Color.PINK);
		}
	}
	
}
