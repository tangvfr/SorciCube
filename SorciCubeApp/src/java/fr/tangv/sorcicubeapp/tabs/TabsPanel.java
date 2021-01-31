package fr.tangv.sorcicubeapp.tabs;

import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.others.PanelOthers;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class TabsPanel extends JTabbedPane {

	private static final long serialVersionUID = -4754481175431522229L;

	//tabbed
	private final CardsPanel cardsPanel;
	private final PanelOthers others;
	
	public TabsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException, PanelFilterException {
		this.setTabPlacement(JTabbedPane.TOP);
		//init tabbed
		this.cardsPanel = new CardsPanel(client, logi);
		this.others = new PanelOthers(client, logi);
		
		//add tabbbed
		this.addTab("Cards", this.cardsPanel);
		JPanel panelOthers = new JPanel(new GridBagLayout());
		panelOthers.add(this.others);
		this.addTab("Others", panelOthers);
		
		//refresh
		this.cardsPanel.refresh();
		
		
	}
	
	
	
}
