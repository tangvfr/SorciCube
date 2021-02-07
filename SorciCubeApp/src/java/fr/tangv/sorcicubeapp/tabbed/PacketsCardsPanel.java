package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

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
	
	private final HandlerPacketCards handler;
	private final JList<PacketCards> listPakcets;
	private final PacketCardsPanel packetCard;
	
	public PacketsCardsPanel(SorciClient client, FrameLogi logi) throws IOException, ReponseRequestException, RequestException {
		super(HORIZONTAL_SPLIT);
		this.handler = new HandlerPacketCards(client);
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
			}
		});
		this.packetCard = new PacketCardsPanel(this);
		this.add(new JScrollPane(this.listPakcets), 0);
		this.add(this.packetCard, 1);
		refresh();
	}
	
	public void refresh() throws IOException, ReponseRequestException, RequestException {
		String nameSelect = this.packetCard.getPacketName();
		handler.refresh();
		Vector<PacketCards> list = new Vector<PacketCards>();
		PacketCards pack = null;
		Enumeration<String> names = handler.getEnumNamePacket();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			PacketCards packet = handler.getPacketCards(name);
			if (name.equals(nameSelect))
				pack = packet;
			list.add(pack);
		}
		listPakcets.setListData(list);
		if (pack != null)
			listPakcets.setSelectedValue(pack, true);
		packetCard.showPacket(pack);
		this.repaint();
	}
	
}
