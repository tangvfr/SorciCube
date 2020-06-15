package fr.tangv.sorcicubespell.logi;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubespell.carts.Carts;
import fr.tangv.sorcicubespell.manager.MongoDBManager;

public class CartsPanel extends JPanel {
	
	private static final long serialVersionUID = 2411422756632892561L;
	private MongoDBManager mongo;
	private Carts carts;
	private FrameLogi frameLogi;
	private PanelNav nav;
	private JPanel edit;
	private JTable table;
	
	public CartsPanel(MongoDBManager mongo, FrameLogi frameLogi) {
		this.frameLogi = frameLogi;
		this.mongo = mongo;
		this.carts = new Carts(mongo);
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
	
	public Carts getCarts() {
		return carts;
	}
	
	public JTable getTable() {
		return table;
	}
	
	public void refrech() {
		this.mongo.refrech();
		this.carts = new Carts(mongo);
		this.nav.refrech();
		if (table.getModel() instanceof ModelEditCart)
			table.setModel(new ModelEditCart(this.carts.getCart(((ModelEditCart) table.getModel()).getCart().getUUID())));
	}

}
