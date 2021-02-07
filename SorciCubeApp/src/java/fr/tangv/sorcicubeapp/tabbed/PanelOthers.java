package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.tools.ImageTool;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PanelOthers extends JPanel {

	private static final long serialVersionUID = -1979640189589135131L;

	public PanelOthers(SorciClient client, FrameLogi logi, TabbedPanel tab) {
		this.setLayout(new GridLayout(4, 1, 10, 10));
		this.setBorder(new TitledBorder("Others"));
		Dimension dim = new Dimension(220, 180);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
		
		//refresh
		JButton refresh = new JButton("Refresh All");
		refresh.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					tab.refreshAll();
					JOptionPane.showMessageDialog(PanelOthers.this, "Successful refresh !", "Refresh All", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException | ReponseRequestException | RequestException | DeckException e1) {
					e1.printStackTrace();
					logi.showConnection("RefreshAllError: "+e1.getMessage(), Color.RED);
				}
			}
		});
		this.add(refresh);
		
		//mineSkin
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
		this.add(mineskin);
		
		//imageTool
		JButton imageTool = new JButton("Image Tool");
		imageTool.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(PanelOthers.this, new ImageTool(), "Image Tool", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.add(imageTool);
		
		//disconnect
		JButton disconnect = new JButton("Disconnect");
		disconnect.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					logi.showConnection("DisconnectError: "+e1.getMessage(), Color.RED);
				}
			}
		});
		this.add(disconnect);
	}
	
}
