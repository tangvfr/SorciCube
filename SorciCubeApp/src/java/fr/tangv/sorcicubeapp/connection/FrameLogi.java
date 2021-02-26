package fr.tangv.sorcicubeapp.connection;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.tangv.sorcicubeapp.tabbed.TabbedPanel;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;
import fr.tangv.sorcicubecore.util.RenderException;

public class FrameLogi extends JFrame {

	private static final long serialVersionUID = -3539638134870583981L;
	private final ConnectionPanel connectionPanel;
	private final JLabel waitConnection;
	private volatile boolean used;
	
	public FrameLogi(String defaultURI) {
		this.connectionPanel = new ConnectionPanel(this, defaultURI);
		this.waitConnection = new JLabel("Wait connection...");
		this.waitConnection.setHorizontalAlignment(JLabel.CENTER);
		//init
		super.setName("Frame");
		super.setTitle("SorciCubeApp");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//default
		showConnection("", Color.BLACK);
	}
	
	public synchronized void showConnection(String message, Color color) {
		used = false;
		this.connectionPanel.setMessage(message, color);
		showContainer(this.connectionPanel);
	}
	
	private synchronized void showContainer(Container container) {
		super.setSize(500, 280);
		super.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(container);
		this.repaint();
	}
	
	public synchronized boolean tryConnection(SorciClientURI uri) {
		if (used) 
			return false;
		used = true;
		showContainer(this.waitConnection);
		try {
			SorciClient client = new SorciClient(uri, 5_000) {
				
				@Override
				public void disconnected() {
					showConnection("Disconnected", Color.GREEN);
				}
				
				@Override
				public void connected() {
					try {
						FrameLogi.this.setContentPane(new TabbedPanel(this, FrameLogi.this));
						FrameLogi.this.setResizable(true);
					} catch (Exception e) {
						showConnection("Error: "+e.getMessage(), Color.RED);
						System.err.println(RenderException.renderException(e));
					}
				}
				
			};
			client.setPrintStream(System.out);
			client.start();
		} catch (Exception e) {
			showConnection("Error: "+e.getMessage(), Color.RED);
			System.err.println(RenderException.renderException(e));
		}
		return true;
	}

}
