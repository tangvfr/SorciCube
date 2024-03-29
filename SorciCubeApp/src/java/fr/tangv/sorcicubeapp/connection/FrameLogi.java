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

public class FrameLogi {

	private final ConnectionPanel connectionPanel;
	private final JLabel waitConnection;
	private final JFrame connect;
	private final JFrame panel;
	private volatile boolean used;
	private volatile boolean err;
	private volatile SorciClient client;
	
	public FrameLogi(String defaultURI) {
		this.connectionPanel = new ConnectionPanel(this, defaultURI);
		this.waitConnection = new JLabel("Wait connection...");
		this.waitConnection.setHorizontalAlignment(JLabel.CENTER);
		//init
		connect = new JFrame("SorciCubeApp Connect");
		connect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connect.setSize(500, 300);
		connect.setResizable(false);
		connect.setLocationRelativeTo(null);
		//panel
		panel = new JFrame("SorciCubeApp Panel");
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setResizable(true);
		panel.setSize(800, 450);
		panel.setVisible(false);
		//default
		showConnection("Welcome", Color.BLUE);
		connect.setVisible(true);
	}
	
	@Deprecated
	public JFrame getFrameConnect() {
		return connect;
	}
	
	public JFrame getFramePanel() {
		return panel;
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
		connect.setContentPane(container);
		connect.setVisible(true);
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
				public synchronized void disconnected() {
					FrameLogi.this.client = null;
					if (!err)
						showConnection("Disconnected", Color.GREEN);
					if (panel.isVisible()) {
						connect.setLocationRelativeTo(panel);
						panel.setVisible(false);
					}
				}
				
				@Override
				public synchronized void connected() {
					try {
						panel.setLocationRelativeTo(connect);
						panel.setContentPane(new TabbedPanel(this, FrameLogi.this));
						panel.setVisible(true);
						connect.setVisible(false);
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
