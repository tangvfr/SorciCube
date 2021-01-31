package fr.tangv.sorcicubeapp.tabs;

import java.io.IOException;

import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class TabsPanel extends JTabbedPane {

	private static final long serialVersionUID = -4754481175431522229L;

	private final SorciClient client;
	private final FrameLogi logi;
	//tabbed
	private final CardsPanel cardsPanel;
	
	public TabsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException, PanelFilterException {
		this.client = client;
		this.logi = logi;
		this.setTabPlacement(JTabbedPane.TOP);
		
		//init tabbed
		this.cardsPanel = new CardsPanel(client, logi);
		
		
		//add tabbbed
		this.addTab("Cards", this.cardsPanel);
		
		
		//refresh
		this.cardsPanel.refresh();
		
		
	}
	
	
	
}
