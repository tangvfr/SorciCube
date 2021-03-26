package fr.tangv.sorcicubeapp.connection;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.tangv.sorcicubeapp.tabbed.TabbedPanel;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;

public class FrameLogi extends JFrame {

	private static final long serialVersionUID = -3539638134870583981L;
	private final ConnectionPanel connectionPanel;
	private final JLabel waitConnection;
	private volatile boolean used;
	private volatile boolean err;
	private volatile SorciClient client;
	
	public FrameLogi(String defaultURI) {
		this.connectionPanel = new ConnectionPanel(this, defaultURI);
		this.waitConnection = new JLabel("Wait connection...");
		this.waitConnection.setHorizontalAlignment(JLabel.CENTER);
		//init
		super.setName("Frame");
		super.setTitle("SorciCubeApp");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//default
		showConnection("Welcome", Color.BLUE);
	}
	
	public synchronized void showConnection(String message, Color color) {
		if (client != null) {
			err = true;
			client.disconnect();
		}
		used = false;
		this.connectionPanel.setMessage(message, color);
		showContainer(this.connectionPanel);
	}
	
	private synchronized void showContainer(Container container) {
		super.setSize(500, 300);
		super.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(container);
		this.repaint();
	}
	
	public synchronized boolean tryConnection(SorciClientURI uri) {
		if (used || client != null)
			return false;
		used = true;
		err = false;
		showContainer(this.waitConnection);
		try {
			this.client = new SorciClient(uri, 5_000) {
				
				@Override
				public void disconnected() {
					FrameLogi.this.client = null;
					if (!err)
						showConnection("Disconnected", Color.GREEN);
				}
				
				@Override
				public void connected() {
					try {
						FrameLogi.this.setContentPane(new TabbedPanel(this, FrameLogi.this));
						FrameLogi.this.setResizable(true);
					} catch (Exception e) {
						showConnection("Error: "+e.getMessage(), Color.RED);
						e.printStackTrace();
					}
				}
				
				@Override
				public void handlingRequest(Client client, Request request) throws Exception {
					if (request.requestType == RequestType.IDENTIFICATION_REFUSED) {
						err = true;
						showConnection(request.data, Color.RED);
					}
				}
				
			};
			client.setPrintStream(System.out);
			client.start();
		} catch (Exception e) {
			err = true;
			showConnection("Error: "+e.getMessage(), Color.RED);
			e.printStackTrace();
		}
		return true;
	}

}
