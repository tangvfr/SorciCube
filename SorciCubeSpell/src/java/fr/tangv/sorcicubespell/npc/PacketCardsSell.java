package fr.tangv.sorcicubespell.npc;

import java.util.Arrays;

import org.bukkit.Material;

import fr.tangv.sorcicubecore.configs.GuiSellerPacketsGuiConfig;
import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class PacketCardsSell extends PCSell {

	private final PacketCards packetCards;
	
	public PacketCardsSell(SorciCubeSpell sorci, int price, String id) {
		super(sorci, price);
		this.packetCards = sorci.getManagerPakcetCards().getPacketCards(id);
		GuiSellerPacketsGuiConfig gui = sorci.config().gui.guiSellerPackets;
		if (isValid()) {
			this.itemView = sorci.getManagerPakcetCards().packetToItem(this.packetCards);
			this.initItemSell(gui, true, this.packetCards.getName());
		} else {
			this.itemView = ItemBuild.buildItem(Material.SIGN, 1, (short) 0, (byte) 0, gui.packetError.value, Arrays.asList(id), false);
		}
	}
	
	@Override
	public boolean buy(PlayerGui player) {
		player.getPlayerFeatures().removeMoney(price);
		player.uploadPlayerFeatures(sorci.getHandlerPlayers());
		player.getPlayer().getInventory().addItem(this.itemView);
		player.getPlayer().sendMessage(sorci.config().messages.packetBuyPacket.value
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
