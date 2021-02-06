package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
				showPanel(listPakcets.getSelectedValue());
			}
		});
		this.add(new JScrollPane(this.listPakcets), 0);
		refresh(null);
	}
	
	public void refresh(String nameSelect) throws IOException, ReponseRequestException, RequestException {
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
		listPakcets.setSelectedValue(pack, true);
		showPanel(pack);
		this.repaint();
	}
	
	public void showPanel(PacketCards packet) {
		if (packet == null) {
			this.add(new JPanel(), 1);
			return;
		}
			
	}
	
}
