package fr.tangv.sorcicubespell.logi;

import java.awt.Color;

import javax.swing.JFrame;

import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.RenderException;

public class FrameLogi extends JFrame {

	private static final long serialVersionUID = -3539638134870583981L;
	private ManagerLogi managerLogi;
	private ConnectionPanel connectionPanel;
	
	public FrameLogi(ManagerLogi managerLogi) {
		this.managerLogi = managerLogi;
		this.connectionPanel = new ConnectionPanel(this);
		//init
		super.setName("Frame");
		super.setTitle("LogiSpell");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//default
		showConnection();
	}
	
	public void showConnection() {
		super.setSize(500, 340);
		super.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(this.connectionPanel);
		this.repaint();
	}
	
	public void tryConnection(String mongoURI, String database) {
		try {
			MongoDBManager mongo = new MongoDBManager(mongoURI, database);
			CardsPanel panel = new CardsPanel(mongo, this);
			this.setContentPane(panel);
			super.setResizable(true);
			panel.refresh();
		} catch (Exception e) {
			this.connectionPanel.setMessage("Error: "+e.getMessage(), Color.RED);
			showConnection();
			System.err.println(RenderException.renderException(e));
		}
	}
	
	public ManagerLogi getManagerLogi() {
		return managerLogi;
	}

}
