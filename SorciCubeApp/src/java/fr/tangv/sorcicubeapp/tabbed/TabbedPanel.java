package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class TabbedPanel extends JTabbedPane {

	private static final long serialVersionUID = -4754481175431522229L;

	//tabbed
	private final CardsPanel cardsPanel;
	private final DecksPanel decksPanel;
	private final PacketsCardsPanel packetsPanel;
	private final ConfigsPanel configPanel;
	private final PlayersPanel playersPanel;
	private final ServerPanel serverPanel;
	private final PanelOthers others;
	
	public TabbedPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException, PanelFilterException, DeckException {
		this.setTabPlacement(JTabbedPane.TOP);
		//init tabbed
		this.cardsPanel = new CardsPanel(client, logi);
		this.decksPanel = new DecksPanel(cardsPanel);
		this.packetsPanel = new PacketsCardsPanel(client, logi);
		this.configPanel = new ConfigsPanel(client, logi);
		this.playersPanel = new PlayersPanel(cardsPanel);
		this.serverPanel = new ServerPanel(client);
		this.others = new PanelOthers(client, logi, this);
		
		//add tabbbed
		this.addTab("Cards", this.cardsPanel);
		this.addTab("DefaultDecks", this.decksPanel);  
		this.addTab("Packets", this.packetsPanel);
		this.addTab("Configs", this.configPanel);
		this.addTab("Players", this.playersPanel);
		this.addTab("Server", this.serverPanel);
		JPanel panelOthers = new JPanel(new GridBagLayout());
		panelOthers.add(this.others);
		this.addTab("Others", panelOthers);
	}
	
	public void refreshAll() throws IOException, ReponseRequestException, RequestException, DeckException {
		this.cardsPanel.refresh();
		this.decksPanel.refresh();
		this.packetsPanel.refresh();
		this.configPanel.refresh();
		this.serverPanel.refresh();
	}
	
}
