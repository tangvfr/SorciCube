package fr.tangv.sorcicubespell.npc;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class PacketCardsSell extends PCSell {

	private PacketCards packetCards;
	
	public PacketCardsSell(SorciCubeSpell sorci, ConfigurationSection config, int price, String id) {
		super(sorci, config, price);
		this.packetCards = sorci.getManagerPakcetCards().getPacketCards(id);
		if (this.isValid()) {
			this.itemView = sorci.getManagerPakcetCards().packetToItem(this.packetCards);
			this.initItemSell("packet", this.packetCards.getName());
		} else {
			this.itemView = ItemBuild.buildItem(Material.SIGN, 1, (short) 0, (byte) 0, config.getString("packet_error"), Arrays.asList(id), false);
		}
	}
	
	@Override
	public boolean buy(PlayerGui player) {
		player.getPlayerFeatures().removeMoney(price);
		player.uploadPlayerFeatures(sorci.getHandlerPlayers());
		player.getPlayer().getInventory().addItem(this.itemView);
		player.getPlayer().sendMessage(getMessage("message_packet_buy_packet")
				.replace("{name}", packetCards.getName())
				.replace("{price}", Integer.toString(price))
		);
		return true;
	}
	
	@Override
	public boolean isValid() {
		return packetCards != null;
	}
	
}
