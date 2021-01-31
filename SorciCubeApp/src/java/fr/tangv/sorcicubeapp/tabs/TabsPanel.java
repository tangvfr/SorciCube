package fr.tangv.sorcicubeapp.tabs;

import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class TabsPanel extends JTabbedPane {

	private static final long serialVersionUID = -4754481175431522229L;

	private final SorciClient client;
	private final FrameLogi logi;
	
	public TabsPanel(SorciClient client, FrameLogi logi) {
		this.client = client;
		this.logi = logi;
		this.setTabPlacement(JTabbedPane.TOP);
		//this.addTab();
	}
	
	
	
}
