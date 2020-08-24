package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.MongoDBManager;

public class CardsPanel extends JPanel {
	
	private static final long serialVersionUID = 2411422756632892561L;
	private MongoDBManager mongo;
	private ManagerCards cards;
	private FrameLogi frameLogi;
	private PanelNav nav;
	private JPanel edit;
	private JTable table;
	private Card card;
	
	public CardsPanel(MongoDBManager mongo, FrameLogi frameLogi) throws Exception {
		this.frameLogi = frameLogi;
		this.mongo = mongo;
		this.cards = new ManagerCards(mongo);
		//edit
		this.edit = new JPanel();
		this.table = new JTable();
		table.addMouseListener(new MouseTable(table, this, frameLogi));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		edit.setLayout(new BorderLayout());
		edit.add(table, BorderLayout.CENTER);
		//nav
		this.nav = new PanelNav(this);
		//split
		super.setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.add(nav);
		split.add(edit);
		super.add(split, BorderLayout.CENTER);
	}

	public MongoDBManager getMongo() {
		return mongo;
	}

	public FrameLogi getFrameLogi() {
		return frameLogi;
	}
	
	public ManagerCards getCards() {
		return cards;
	}
	
	public void setCard(Card card) {
		this.card = card;
		if (card != null) {
			edit.setBorder(new TitledBorder(PanelNav.renderHTMLCard(card, "Card: ")));
			table.setModel(new ModelEditCard(card));
		} else {
			edit.setBorder(new TitledBorder("Card: none"));
			table.setModel(new DefaultTableModel());
		}
	}
	
	public PanelNav getPanelNav() {
		return nav;
	}
	
	public void refresh() {
		this.cards.refresh();
		this.nav.refresh();
		if (card != null) {
			card = this.cards.getCard(card.getUUID());
			setCard(card);
			if (card != null)
				nav.setCardSelectedInList(card);
		}
		this.repaint();
	}

}
