package fr.tangv.sorcicubeapp.tabs;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.card.PanelFilter.PanelFilterException;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.tools.ImageTool;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class TabsPanel extends JTabbedPane {

	private static final long serialVersionUID = -4754481175431522229L;

	private final SorciClient client;
	private final FrameLogi logi;
	//tabbed
	private final CardsPanel cardsPanel;
	private final JPanel others;
	
	public TabsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException, PanelFilterException {
		this.client = client;
		this.logi = logi;
		this.setTabPlacement(JTabbedPane.TOP);
		
		//init tabbed
		this.cardsPanel = new CardsPanel(client, logi);
		//other
		this.others = new JPanel();
		this.others.setLayout(new BoxLayout(others, BoxLayout.Y_AXIS));
		JButton mineskin = new JButton("mineskin.org");
		mineskin.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE))
					try {
						Desktop.getDesktop().browse(new URI("https://mineskin.org/"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
			}
		});
		others.add(mineskin);
		JButton imageTool = new JButton("Image Tool");
		imageTool.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(others, new ImageTool(), "Image Tool", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		others.add(imageTool);
		JButton disconnect = new JButton("Disconnect");
		disconnect.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					cardsPanel.getFrameLogi().showConnection("DisconnectError: "+e1.getMessage(), Color.RED);
				}
			}
		});
		others.add(disconnect);
		//add tabbbed
		this.addTab("Cards", this.cardsPanel);
		this.addTab("Others", this.others);
		
		//refresh
		this.cardsPanel.refresh();
		
		
	}
	
	
	
}
