package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.MongoDBManager;

public class CardsPanel extends JPanel {
	
	private static final long serialVersionUID = 2411422756632892561L;
	private MongoDBManager mongo;
	private ManagerCards carts;
	private FrameLogi frameLogi;
	private PanelNav nav;
	private JPanel edit;
	private JTable table;
	
	public CardsPanel(MongoDBManager mongo, FrameLogi frameLogi) {
		this.frameLogi = frameLogi;
		this.mongo = mongo;
		this.carts = new ManagerCards(mongo);
		//edit
		this.edit = new JPanel();
		this.table = new JTable();
		table.addMouseListener(new MouseTable(table, this));
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
	
	public ManagerCards getCarts() {
		return carts;
	}
	
	public JTable getTable() {
		return table;
	}
	
	public PanelNav getPanelNav() {
		return nav;
	}
	
	public void refrech() {
		this.carts.refresh();
		this.nav.refrech();
		if (table.getModel() instanceof ModelEditCard)
			table.setModel(new ModelEditCard(this.carts.getCard(((ModelEditCard) table.getModel()).getCard().getUUID())));
	}

}
