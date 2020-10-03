package fr.tangv.sorcicubespell.npc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.packet.PacketCards;

public class PacketCardsSell {

	private PacketCards packetCards;
	private ItemStack itemPacket;
	private ItemStack itemSell;
	private int price;
	private boolean valid;
	
	public PacketCardsSell(SorciCubeSpell sorci, ConfigurationSection config, String name) {
		this.price = config.getInt(name);
		
		
	}

	public PacketCards getPacketCards() {
		return packetCards;
	}

	public ItemStack getItemPacket() {
		return itemPacket;
	}

	public ItemStack getItemSell() {
		return itemSell;
	}

	public int getPrice() {
		return price;
	}

	public boolean isValid() {
		return valid;
	}
	
}
