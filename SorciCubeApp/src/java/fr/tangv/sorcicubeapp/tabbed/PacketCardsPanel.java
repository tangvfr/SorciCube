package fr.tangv.sorcicubeapp.tabbed;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubeapp.utils.ColorMCToHTML;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.packet.PacketCards;

public class PacketCardsPanel extends JScrollPane {

	private static final long serialVersionUID = 2848497329580995498L;
	private final PacketsCardsPanel manager;
	private PacketCards packet;
	private final JPanel empty;
	private final JPanel packetPanel;
	private final ComponentText name;
	private final ComponentNumberInt id;
	private final ComponentArrayInt factions;
	private final ComponentArrayInt rarities;
	private final ComponentArrayInt types;
	private final ComponentNumberInt size;
	private final JButton apply;
	private final JButton cancel;

	public PacketCardsPanel(PacketsCardsPanel manager) {
		this.manager = manager;
		this.empty = new JPanel();
		this.packetPanel = new JPanel();
		//pakcet
		this.name = new ComponentText("Name");
		this.id = new ComponentNumberInt("ItemID");
		this.factions = new ComponentArrayInt(CardFaction.values(), "Factions");
		this.rarities = new ComponentArrayInt(CardRarity.values(), "Rarities");
		this.types = new ComponentArrayInt(CardType.values(), "Types");
		this.size = new ComponentNumberInt("Size");
		//button
		this.apply = new JButton("Apply");
		this.apply.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		this.cancel = new JButton("Cancel");
		
		JPanel btns = new JPanel(new BorderLayout(10, 10));
		btns.add(apply, BorderLayout.WEST);
		btns.add(cancel, BorderLayout.EAST);
		//panel
		this.packetPanel.setLayout(new BoxLayout(packetPanel, BoxLayout.X_AXIS));
		this.packetPanel.add(name);
		this.packetPanel.add(id);
		this.packetPanel.add(factions);
		this.packetPanel.add(rarities);
		this.packetPanel.add(types);
		this.packetPanel.add(size);
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
			this.name.setText(packet.getName());
			this.id.setInt(packet.getId());
			this.factions.setInts(packet.getFaction());
			this.rarities.setInts(packet.getRarity());
			this.types.setInts(packet.getType());
			this.size.setInt(packet.getSize());
			packetPanel.setBorder(new TitledBorder(ColorMCToHTML.replaceColor("Packet: "+packet.getName())));
			this.setViewportView(packetPanel);
		}
	}
	
	public PacketCards createPacket() {
		return new PacketCards(name.getText(), factions.getInts(), rarities.getInts(), types.getInts(), size.getInt(), id.getInt());
	}
	
}
