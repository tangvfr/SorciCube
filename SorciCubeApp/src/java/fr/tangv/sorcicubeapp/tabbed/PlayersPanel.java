package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubeapp.card.CardsPanel;
import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.tools.ExceptionPlayerResources;
import fr.tangv.sorcicubeapp.tools.PlayerResources;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;

public class PlayersPanel extends JPanel {

	private static final long serialVersionUID = 933468304672805743L;
	private final FrameLogi logi;
	private final HandlerPlayers handler;
	private final JButton find;
	private final JLabel clear;
	private final JTextField search;
	private final JScrollPane center;
	private final JPanel empty;
	
	public PlayersPanel(CardsPanel cardsPanel) throws IOException, ReponseRequestException, RequestException {
		this.logi = cardsPanel.getFrameLogi();
		this.handler = new HandlerPlayers(cardsPanel.getClient(), cardsPanel.getCards());
		//refresh
		this.find = new JButton("Find | ");
		this.find.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				find();
			}
		});
		//clear
		this.clear = new JLabel(" X ");
		clear.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search.setText("");
				find();
			}
		});
		clear.setForeground(Color.RED);
		//search
		this.search = new JTextField();
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					find();
				}
			}
		});
		//makeup
		this.setLayout(new BorderLayout());
		JPanel panelUp = new JPanel(new BorderLayout());
		panelUp.add(find, BorderLayout.NORTH);
		panelUp.add(clear, BorderLayout.WEST);
		panelUp.add(search, BorderLayout.CENTER);
		panelUp.setBorder(new EmptyBorder(0, 0, 5, 0));
		this.add(panelUp, BorderLayout.NORTH);
		this.empty = new JPanel();
		this.center = new JScrollPane(empty);
		this.add(center, BorderLayout.CENTER);
	}

	public void find() {
		try {
			UUID uuid;
			try {
				uuid = UUID.fromString(find.getText());
			} catch (IllegalArgumentException e) {
				try {
					uuid = PlayerResources.findUserName(find.getText());
				} catch (ExceptionPlayerResources e1) {
					uuid = null;
				}
			}
			if (uuid == null) {
				find.setText("Find | Pseudo not found to Mojang");
			} else {
				try {
					PlayerResources res = new PlayerResources(uuid);
					if (handler.containtPlayer(uuid)) {
						PlayerFeature feature = handler.getPlayer(uuid, res.getName());
						find.setText("Find | lvl."+feature.getLevel()+" "+feature.getPseudo());
						center.setViewportView(new PlayerHeadList(res, feature));
						return;
					} else {
						find.setText("Find | "+res.getName()+" is not registered on server");
					}
					this.repaint();
				} catch (ExceptionPlayerResources e) {
					find.setText("Find | UUID not found to Mojang");
				}
			}
			center.setViewportView(empty);
			this.repaint();
		} catch (IOException | ReponseRequestException | RequestException | DeckException e) {
			JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error Find", JOptionPane.ERROR_MESSAGE);
			logi.showConnection("Error: "+e.getMessage(), Color.MAGENTA);
		}
	}
	
	private class PlayerHeadList extends JPanel {

		private static final long serialVersionUID = -6207825311939478548L;
		
		private PlayerHeadList(PlayerResources res, PlayerFeature feature) throws ExceptionPlayerResources, IOException {
			int layout = 5;
			this.setLayout(new BorderLayout(layout, layout));
			JLabel head = new JLabel(new ImageIcon(res.getHead().getScaledInstance(128, 128, BufferedImage.SCALE_DEFAULT)));
			int headBorder = 10;
			head.setBorder(new EmptyBorder(headBorder, headBorder, headBorder, headBorder));
			this.add(head, BorderLayout.WEST);
			this.add(new JLabel(
					  "<html><body>"
					+ "<h1>"+res.getName()+"</h1>"
					+ "<br>"
					+ "<h6>"+res.getUUID().toString()+"</h6>"
					+ "</body></html>"
			), BorderLayout.CENTER);
		}
		
		
	}

}
