package fr.tangv.sorcicubeapp.connection;

import java.awt.Color;

import javax.swing.JFrame;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;
import fr.tangv.sorcicubecore.util.RenderException;

public class FrameLogi extends JFrame {

	private static final long serialVersionUID = -3539638134870583981L;
	private ConnectionPanel connectionPanel;
	
	public FrameLogi() {
		this.connectionPanel = new ConnectionPanel(this);
		//init
		super.setName("Frame");
		super.setTitle("LogiSpell");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//default
		showConnection("", Color.BLACK);
	}
	
	public void showConnection(String message, Color color) {
		this.connectionPanel.setMessage(message, color);
		super.setSize(500, 340);
		super.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(this.connectionPanel);
		this.repaint();
	}
	
	public void tryConnection(SorciClientURI uri) {
		try {
			SorciClient client = new SorciClient(uri, 5_000) {
				
				@Override
				public void disconnected() {
					showConnection("Disconnected", Color.ORANGE);
				}
				
				@Override
				public void connected() {
					try {
						CardsPanel panel = new CardsPanel(this, FrameLogi.this);
						FrameLogi.this.setContentPane(panel);
						FrameLogi.this.setResizable(true);
						panel.refresh();
					} catch (Exception e) {
						showConnection("Error: "+e.getMessage(), Color.RED);
						System.err.println(RenderException.renderException(e));
					}
				}
				
			};
			client.start();
		} catch (Exception e) {
			showConnection("Error: "+e.getMessage(), Color.RED);
			System.err.println(RenderException.renderException(e));
		}
	}

}
