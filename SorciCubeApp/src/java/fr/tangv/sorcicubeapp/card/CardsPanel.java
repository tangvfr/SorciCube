package fr.tangv.sorcicubeapp.card;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class CardsPanel extends JPanel {
	
	private static final long serialVersionUID = 2411422756632892561L;
	private SorciClient client;
	private HandlerCards cards;
	private FrameLogi frameLogi;
	private PanelNav nav;
	private JPanel edit;
	private JTable table;
	private Card card;
	
	public CardsPanel(SorciClient client, FrameLogi frameLogi) throws IOException, ReponseRequestException, RequestException, PanelFilterException {
		this.frameLogi = frameLogi;
		this.client = client;
		this.cards = new HandlerCards(client);
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
		refresh();
	}

	public SorciClient getClient() {
		return client;
	}

	public FrameLogi getFrameLogi() {
		return frameLogi;
	}
	
	public HandlerCards getCards() {
		return cards;
	}
	
	public void setCard(Card card) {
		this.card = card;
		if (card != null) {
			edit.setBorder(new TitledBorder(PanelNav.renderHTMLCard(card, "Card: ")));
			table.setModel(new ModelEditCard(card, cards));
		} else {
			edit.setBorder(new TitledBorder("Card: none"));
			table.setModel(new DefaultTableModel());
		}
	}
	
	public PanelNav getPanelNav() {
		return nav;
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
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
