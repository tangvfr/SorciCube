package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.handler.HandlerPacketCards;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PacketsCardsPanel extends SearchPanel<PacketCards> {

	private static final long serialVersionUID = 2460248920367644846L;
	
	private final HandlerPacketCards handler;
	private final PacketCardsPanel packetCard;
	
	public PacketsCardsPanel(SorciClient client, FrameLogi logi) throws IOException, ResponseRequestException, RequestException {
		super(client, logi);
		this.handler = new HandlerPacketCards(client);
		this.packetCard = new PacketCardsPanel(this);
		//popmenu
		JMenuItem create = new JMenuItem("Create Pakcet");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					String name = JOptionPane.showInputDialog(logi.getFramePanel(), "Name for packet new", "Create Packet", JOptionPane.QUESTION_MESSAGE);
					if (name != null && !name.isEmpty())
						try {
							handler.newPacket(name);
							refresh();
						} catch (IOException | ResponseRequestException | RequestException e1) {
							JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e1.getMessage(), "Create Packet", JOptionPane.ERROR_MESSAGE);
						}
				}
			}
		});
		JMenuItem delete = new JMenuItem("Remove Pakcet");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					PacketCards packet = list.getSelectedValue();
					if (packet != null) {
						if (0 == JOptionPane.showConfirmDialog(logi.getFramePanel(), "Are you sure remove pakcet nommed \""+packet.getName()+"\" ?", "Remove Packet", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
							try {
								handler.removePacket(packet.getName());
								refresh();
							} catch (IOException | ResponseRequestException | RequestException e1) {
								JOptionPane.showMessageDialog(logi.getFramePanel(), "Error: "+e1.getMessage(), "Remove Packet", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(logi.getFramePanel(), "No selected packet ?", "Remove Packet", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		//menu add
		menu.add(create);
		menu.add(delete);
		this.addMainComponent(packetCard);
		refresh();
	}
			
	@Override
	public void refresh() {
		try {
			String nameSelect = this.packetCard.getPacketName();
			handler.refresh();
			int max = 0;
			Vector<PacketCards> list = new Vector<PacketCards>();
			PacketCards pack = null;
			String searchName = search.getText().toLowerCase();
			for (String name : handler.getPackets()) {
				PacketCards packet = handler.getPacketCards(name);
				if (name.equals(nameSelect))
					pack = packet;
				if (searchName.isEmpty() || name.toLowerCase().contains(searchName))
					list.add(packet);
				max++;
			}
			this.list.setListData(list);
			if (pack != null)
				this.list.setSelectedValue(pack, true);
			packetCard.showPacket(pack);
			refresh.setText("Refresh | "+max+" packets "+list.size()+" find");
			this.repaint();
		} catch (IOException | ResponseRequestException | RequestException e) {
			warningBug(e, "refresh");
		}
	}
	
	public void updatePacket(String lastName, PacketCards newPacket) {
		try {
			handler.updatePacket(lastName, newPacket);
		} catch (IOException | ResponseRequestException | RequestException e) {
			warningBug(e, "update");
		}
	}

	private String antiSpaces(String input) {
		if (input.startsWith(" "))
			return input.replaceFirst(" ", "§4[*space*]§0");
		else
			return input;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends PacketCards> list, PacketCards value, int index, boolean isSelected, boolean cellHasFocus) {
		return new JLabel(
				"<html><body><span>"
				+(isSelected ? ">" : "")+"</span>"
				+ColorMCToHTML.replaceColor(antiSpaces(value.getName()))
				+"</html></body>"
			);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		packetCard.showPacket(list.getSelectedValue());
		packetCard.repaint();
	}
	
}