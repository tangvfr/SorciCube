package fr.tangv.sorcicubeapp.tabbed;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class PacketsCardsPanel extends JSplitPane {

	private static final long serialVersionUID = 2460248920367644846L;
	private final JList<PacketCards> listPakcets;
	
	public PacketsCardsPanel(SorciClient client, FrameLogi logi) {
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
				//will make
			}
		});
		this.add(this.listPakcets, 0);
	}
	
	public void refresh() {
		
		
	}
	
}
