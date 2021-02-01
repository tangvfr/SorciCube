package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PakcetCardsPanel extends JSplitPane {

	private static final long serialVersionUID = 2460248920367644846L;
	private final JList<PacketCards> listPakcets;
	
	public PakcetCardsPanel(SorciClient client, FrameLogi logi) {
		this.listPakcets = new JList<PacketCards>();
		this.listPakcets.setCellRenderer(new ListCellRenderer<PacketCards>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends PacketCards> list, PacketCards value, int index, boolean isSelected, boolean cellHasFocus) {
				//I make
				return null;
			}
		});
		this.add(this.listPakcets, 0);
		
	}
	
}
