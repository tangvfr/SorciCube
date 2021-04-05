package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

public class GroupPanel extends JScrollPane {

	private static final long serialVersionUID = 2848497329580995498L;
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
		this.apply = new JButton("Save");
		this.apply.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PacketCards pack = createPacket();
				manager.updatePacket(packet.getName(), pack);
				manager.refresh();
				showPacket(pack);
				PacketCardsPanel.this.repaint();
			}
		});
		this.cancel = new JButton("Cancel");
		this.cancel.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				initValue();
			}
		});
		JPanel btns = new JPanel(new GridLayout(1, 2, 10, 10));
		btns.add(apply);
		btns.add(cancel);
		//panel
		this.packetPanel.setLayout(new GridBagLayout());
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS)); 
		pan.add(name);
		pan.add(id);
		pan.add(factions);
		pan.add(rarities);
		pan.add(types);
		pan.add(size);
		pan.add(btns);
		this.packetPanel.add(pan);
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
			initValue();
			packetPanel.setBorder(new TitledBorder("<html><body>"+ColorMCToHTML.replaceColor("Packet: "+packet.getName())+"</html></body>"));
			this.setViewportView(packetPanel);
		}
	}
	
	public void initValue() {
		this.name.setText(packet.getName());
		this.id.setInt(packet.getId());
		this.factions.setInts(packet.getFaction());
		this.rarities.setInts(packet.getRarity());
		this.types.setInts(packet.getType());
		this.size.setInt(packet.getSize());
	}
	
	public PacketCards createPacket() {
		return new PacketCards(name.getText(), factions.getInts(), rarities.getInts(), types.getInts(), size.getInt(), id.getInt());
	}
	
}