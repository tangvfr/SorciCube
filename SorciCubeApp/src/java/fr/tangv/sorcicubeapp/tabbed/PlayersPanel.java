package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
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
	private final JLabel message;
	private final File headImage;
	
	public PlayersPanel(CardsPanel cardsPanel) throws IOException, ReponseRequestException, RequestException {
		this.logi = cardsPanel.getFrameLogi();
		this.handler = new HandlerPlayers(cardsPanel.getClient(), cardsPanel.getCards());
		//refresh
		this.find = new JButton("Find");
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
		this.message = new JLabel("");
		message.setHorizontalAlignment(JLabel.CENTER);
		this.center = new JScrollPane(message);
		this.add(center, BorderLayout.CENTER);
		this.headImage = new File(System.getenv("TEMP")+"/head_player_sorcicube.png");
		if (!headImage.exists())
			headImage.createNewFile();
	}

	public void find() {
		if (!find.isEnabled())
			return;
		find.setEnabled(false);
		try {
			UUID uuid;
			String text = search.getText();
			try {
				uuid = UUID.fromString(text);
			} catch (IllegalArgumentException e) {
				try {
					uuid = PlayerResources.findUserName(text);
				} catch (ExceptionPlayerResources e1) {
					uuid = null;
				}
			}
			if (uuid == null) {
				message.setText("<html><body><center><font size=7>"+text+"</font><br><font size=4>Pseudo not found to Mojang</font></center></body></html>");
			} else {
				try {
					PlayerResources res = new PlayerResources(uuid);
					if (handler.containtPlayer(uuid)) {
						PlayerFeature feature = handler.getPlayer(uuid, res.getName());
						message.setText("lvl."+feature.getLevel()+" "+feature.getPseudo());
						center.setViewportView(new PlayerHeadList(res, feature));
						return;
					} else {
						int size = 128;
						BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
						Graphics2D bGr = image.createGraphics();
						bGr.drawImage(res.getHead(size), 0, 0, null);
						bGr.dispose();
						ImageIO.write(image, "png", this.headImage);
						message.setText("<html><body><center><img alt=\"Head Of Player\" src=\"file:"+this.headImage.getPath()+"\"><br><font size=7>"+res.getName()+"</font><br><font size=4>Player is not registered on server</font></center></body></html>");
					}
					this.repaint();
				} catch (ExceptionPlayerResources e) {
					message.setText("<html><body><center><font size=7>"+uuid.toString()+"</font><br><font size=4>UUID not found to Mojang</font></center></body></html>");
				}
			}
			center.setViewportView(message);
			this.repaint();
			new Thread(() -> {
				try {
					Thread.sleep(2000);//for time rate request API Mojang
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				find.setEnabled(true);
				find.repaint();
			}).start();
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
			JLabel head = new JLabel(new ImageIcon(res.getHead(128)));
			int headBorder = 10;
			head.setBorder(new EmptyBorder(headBorder, headBorder, headBorder, headBorder));
			this.add(head, BorderLayout.WEST);
			this.add(new JLabel(
					  "<html><body>"
					+ "<font size=7>"+res.getName()+"</font>"
					+ "<br>"
					+ "<font size=4>"+res.getUUID().toString()+"</font>"
					+ "</body></html>"
			), BorderLayout.CENTER);
		}
		
		
	}

}
