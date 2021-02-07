package fr.tangv.sorcicubeapp.tabbed;

import javax.swing.JPanel;

import fr.tangv.sorcicubecore.packet.PacketCards;

public class PacketCardsPanel extends JPanel {

	private static final long serialVersionUID = 2848497329580995498L;
	private final PacketsCardsPanel manager;
	private PacketCards packet;

	public PacketCardsPanel(PacketsCardsPanel manager) {
		this.manager = manager;
		this.packet = null;
	}
	
	public String getPacketName() {
		if (packet == null)
			return null;
		return packet.getName();
	}
	
	public void showPacket(PacketCards packet) {
		this.packet = packet;
		
		
		
	}
	
}
