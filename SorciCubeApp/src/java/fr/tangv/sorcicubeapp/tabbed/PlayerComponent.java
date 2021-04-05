package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.tools.ExceptionPlayerResources;
import fr.tangv.sorcicubeapp.tools.PlayerResources;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;

public class PlayerComponent extends JComponent {

	private static final long serialVersionUID = -6207825311939478548L;
	private final JLabel head;
	private final JLabel name;
	private final JLabel uuid;
	private final PlayerResources res;
	private PlayerFeatures feature;
	private final HandlerPlayers handler;
	private final ComponentNumberInt lvl;
	private final ComponentNumberInt exp;
	private final ComponentNumberInt money;
	private final ComponentNumberInt decks;
	private final ComponentBoolean admin;
	private final ComponentCombo<String> group;
	
	public PlayerComponent(PlayerResources res, HandlerPlayers handler, Vector<String> groups) throws ExceptionPlayerResources, IOException, ReponseRequestException, RequestException, DeckException {
		this.setLayout(new GridBagLayout());
		this.res = res;
		this.handler = handler;
		this.lvl = new ComponentNumberInt("Level");
		this.exp = new ComponentNumberInt("Experience");
		this.money = new ComponentNumberInt("Money");
		this.decks = new ComponentNumberInt("Decks");
		this.admin = new ComponentBoolean("Admin");
		this.group = new ComponentCombo<String>("Group", groups);
		if (!groups.contains(feature.getGroup()))
			feature.setGroup("");
		getPlayerValues();
		//head
		this.head = new JLabel(new ImageIcon(res.getHead(128)));
		this.head.setAlignmentX(0.5F);
		//name
		this.name = new JLabel(res.getName());
		this.name.setHorizontalAlignment(JLabel.CENTER);
		this.name.setAlignmentX(0.5F);
		this.name.setForeground(Color.DARK_GRAY);
		this.name.setFont(name.getFont().deriveFont(36F));
		this.name.addMouseListener(new ActionCopy(name.getText(), "Copy Name", "You copied Name !"));
		//uuid
		this.uuid = new JLabel(res.getUUID().toString());
		this.uuid.setAlignmentX(0.5F);
		this.uuid.setHorizontalAlignment(JLabel.CENTER);
		this.uuid.setForeground(Color.DARK_GRAY);
		this.uuid.setFont(name.getFont().deriveFont(16F));
		this.uuid.addMouseListener(new ActionCopy(uuid.getText(), "Copy UUID", "You copied UUID !"));
		//information
		JPanel info = new JPanel();
		info.setBorder(new TitledBorder("Information"));
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.setAlignmentX(0.5f);
		info.add(head);
		info.add(name);
		info.add(uuid);
		//features
		JPanel features = new JPanel();
		features.setBorder(new TitledBorder("Features"));
		features.setLayout(new BoxLayout(features, BoxLayout.Y_AXIS));
		features.setAlignmentX(0.5f);
		features.add(lvl);
		features.add(exp);
		features.add(money);
		features.add(decks);
		features.add(admin);
		features.add(group);
		//buttons
		JButton apply = new JButton("Save");
		apply.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					setPlayerValues();
				} catch (IOException | ReponseRequestException | RequestException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(PlayerComponent.this, e1.getMessage(), "Error Save", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton cancel = new JButton("Update");
		cancel.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					getPlayerValues();
				} catch (IOException | ReponseRequestException | RequestException | DeckException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(PlayerComponent.this, e1.getMessage(), "Error Update", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JPanel btns = new JPanel(new GridLayout(1, 2, 10, 10));
		btns.add(apply);
		btns.add(cancel);
		//warning
		JPanel warn = new JPanel(new BorderLayout(0, 0));
		TitledBorder bord = new TitledBorder("Warning");
		bord.setTitleColor(Color.RED);
		JLabel warning = new JLabel("Mustn't fill fields with random values");
		warning.setHorizontalAlignment(JLabel.CENTER);
		warn.setAlignmentX(0.5F);
		warn.setBorder(bord);
		warn.add(warning, BorderLayout.CENTER);
		//JPanel Message
		JPanel message = new JPanel(new BorderLayout(5, 5));
		message.setAlignmentX(0.5F);
		message.setBorder(new TitledBorder("Send Message"));
		JTextField field = new JTextField("{\"text\": \"[SorcicubeAPI] Voici un Message\"}");
		JCheckBox json = new JCheckBox("JSON");
		json.setSelected(true);
		JButton send = new JButton("Send");
		send.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					handler.startSendingPlayer(res.getUUID(), json.isSelected() ? field.getText() : ("{\"text\": \""+field.getText()+"\"}"));
				} catch (IOException | ReponseRequestException | RequestException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(PlayerComponent.this, e1.getMessage(), "Error Send Message", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		message.add(new JLabel("Message:"), BorderLayout.WEST);
		message.add(field, BorderLayout.CENTER);
		message.add(json, BorderLayout.EAST);
		message.add(send, BorderLayout.SOUTH);
		//style
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.add(info);
		pan.add(warn);
		pan.add(features);
		pan.add(btns);
		pan.add(message);
		this.add(pan);
	}
	
	private void getPlayerValues() throws IOException, ReponseRequestException, RequestException, DeckException {
		this.feature = handler.getPlayer(res.getUUID(), res.getName());
		this.lvl.setInt(feature.getLevel());
		this.exp.setInt(feature.getExperience());
		this.money.setInt(feature.getMoney());
		this.decks.setInt(feature.getUnlockDecks());
		this.admin.setBoolean(feature.isAdmin());
		this.group.setSelection(feature.getGroup());
		this.repaint();
	}
	
	@SuppressWarnings("deprecation")
	private void setPlayerValues() throws IOException, ReponseRequestException, RequestException {
		if (this.admin.getBoolean() != feature.isAdmin()) {
			short m = Short.MAX_VALUE/2;
			String valid = Integer.toString((int) (Math.random()*m)+m);
			if (!valid.equals(JOptionPane.showInputDialog(this, "Enter this number \""+valid+"\" for set player isAdmin", "Valid Set Admin", JOptionPane.WARNING_MESSAGE))) {
				JOptionPane.showMessageDialog(this, "Set admin canceled !", "Canceled Set Admin", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		feature.setLevel((byte) this.lvl.getInt());
		feature.setExperience(this.exp.getInt());
		feature.setMoney(this.money.getInt());
		feature.setUnlockDecks(this.decks.getInt());
		feature.setAdmin(this.admin.getBoolean());
		feature.setGroup(this.group.getSelection());
		handler.update(feature);
		handler.startUpdatingPlayer(feature.getUUID());
	}

	private class ActionCopy extends ClickListener {
		
		private final String copy;
		private final String message;
		private final String title;
		
		private ActionCopy(String copy, String title, String message) {
			this.copy = copy;
			this.title = title;
			this.message = message;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copy), null);
			JOptionPane.showMessageDialog(PlayerComponent.this, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
				
	}
	
}
