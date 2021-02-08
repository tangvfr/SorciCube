package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.handler.HandlerPacketCards;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PacketsCardsPanel extends JSplitPane {

	private static final long serialVersionUID = 2460248920367644846L;
	
	private final FrameLogi logi;
	private final HandlerPacketCards handler;
	private final JButton refresh;
	private final JLabel clear;
	private final JTextField search;
	private final JList<PacketCards> listPakcets;
	private final PacketCardsPanel packetCard;
	
	public PacketsCardsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(HORIZONTAL_SPLIT);
		this.logi = logi;
		this.handler = new HandlerPacketCards(client);
		//refresh
		this.refresh = new JButton("");
		this.refresh.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		//clear
		this.clear = new JLabel(" X ");
		clear.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search.setText("");
				refresh();
			}
		});
		clear.setForeground(Color.RED);
		//info
		this.search = new JTextField();
		search.setToolTipText("name of packet");
		search.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					refresh();
				}
			}
		});
		//main panel
		this.listPakcets = new JList<PacketCards>();
		this.listPakcets.setCellRenderer(new ListCellRenderer<PacketCards>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends PacketCards> list, PacketCards value, int index, boolean isSelected, boolean cellHasFocus) {
				return new JLabel(
						"<html><body><span>"
						+(isSelected ? ">" : "")+"</span>"
						+ColorMCToHTML.replaceColor(value.getName())
						+"</html></body>"
					);
			}
		});
		this.listPakcets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listPakcets.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				packetCard.showPacket(listPakcets.getSelectedValue());
				packetCard.repaint();
			}
		});
		this.packetCard = new PacketCardsPanel(this);
		//makeup
		JPanel panelUp = new JPanel(new BorderLayout());
		panelUp.add(refresh, BorderLayout.NORTH);
		panelUp.add(clear, BorderLayout.WEST);
		panelUp.add(search, BorderLayout.CENTER);
		panelUp.setBorder(new EmptyBorder(0, 0, 5, 0));
		JPanel panelLeft = new JPanel(new BorderLayout());
		panelLeft.add(panelUp, BorderLayout.NORTH);
		panelLeft.add(listPakcets, BorderLayout.CENTER);
		this.add(panelLeft, 0);
		this.add(this.packetCard, 1);
		refresh();
		//add popmenu
	}
	
	public void refresh() {
		try {
			String nameSelect = this.packetCard.getPacketName();
			handler.refresh();
			int max = 0;
			Vector<PacketCards> list = new Vector<PacketCards>();
			PacketCards pack = null;
			Enumeration<String> names = handler.getEnumNamePacket();
			String searchName = search.getText();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				PacketCards packet = handler.getPacketCards(name);
				if (name.equals(nameSelect))
					pack = packet;
				if (searchName.isEmpty() || name.contains(searchName))
					list.add(pack);
				max++;
			}
			listPakcets.setListData(list);
			if (pack != null)
				listPakcets.setSelectedValue(pack, true);
			packetCard.showPacket(pack);
			refresh.setText("Refresh | "+max+" packets "+list.size()+" find");
			this.repaint();
		} catch (IOException | ReponseRequestException | RequestException e) {
			JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error refrech", JOptionPane.ERROR_MESSAGE);
			logi.showConnection("Error: "+e.getMessage(), Color.PINK);
		}
	}
	
	public void updatePacket(String lastName, PacketCards newPacket) {
		try {
			handler.updatePacket(lastName, newPacket);
		} catch (IOException | ReponseRequestException | RequestException e) {
			JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error update", JOptionPane.ERROR_MESSAGE);
			logi.showConnection("Error: "+e.getMessage(), Color.PINK);
		}
	}
	
}
