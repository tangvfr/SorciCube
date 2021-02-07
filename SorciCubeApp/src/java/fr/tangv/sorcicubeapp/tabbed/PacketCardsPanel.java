package fr.tangv.sorcicubeapp.tabbed;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.packet.PacketCards;

public class PacketCardsPanel extends JScrollPane {

	private static final long serialVersionUID = 2848497329580995498L;
	private final PacketsCardsPanel manager;
	private PacketCards packet;
	private final JPanel empty;
	private final JPanel packetPanel;
	private final ComponentArrayInt factions;

	public PacketCardsPanel(PacketsCardsPanel manager) {
		this.manager = manager;
		this.empty = new JPanel();
		this.packetPanel = new JPanel();
		this.factions = new ComponentArrayInt(CardFaction.values(), "Factions");
		//panel
		this.packetPanel.add(factions);
		showPacket(null);
	}
	
	public String getPacketName() {
		if (packet == null)
			return null;
		return packet.getName();
	}
	
	public void showPacket(PacketCards packet) {
		this.packet = packet;
		if (packet == null) {
			this.setViewportView(empty);
		} else {
			
			this.setViewportView(packetPanel);
		}
	}
	
}
