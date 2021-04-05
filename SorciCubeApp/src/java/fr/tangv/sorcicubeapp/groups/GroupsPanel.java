package fr.tangv.sorcicubeapp.groups;

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
import fr.tangv.sorcicubeapp.tabbed.PacketCardsPanel;
import fr.tangv.sorcicubeapp.tabbed.SearchPanel;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.handler.HandlerGroups;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class GroupsPanel extends SearchPanel<Group> {

	private static final long serialVersionUID = 2516520893332579991L;
	private final HandlerGroups groups;
	
	public GroupsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(client, logi);
		this.groups = new HandlerGroups(client);
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
						} catch (IOException | ReponseRequestException | RequestException e1) {
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
							} catch (IOException | ReponseRequestException | RequestException e1) {
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
	public Component getListCellRendererComponent(JList<? extends Group> list, Group group, int index, boolean isSelected, boolean cellHasFocus) {
		return new JLabel(
				"<html><body><span>"
				+(isSelected ? ">" : "")+group.getName()+" | </span>"
				+ColorMCToHTML.replaceColor(group.getDisplay())
				+"</html></body>"
			);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		packetCard.showPacket(list.getSelectedValue());
		packetCard.repaint();
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
		} catch (IOException | ReponseRequestException | RequestException e) {
			warningBug(e, "refresh");
		}
	}

}
