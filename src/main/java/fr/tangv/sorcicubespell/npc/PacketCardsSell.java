package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.packet.PacketCards;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class PacketCardsSell {

	private PacketCards packetCards;
	private ItemStack itemPacket;
	private ItemStack itemSell;
	private ItemStack itemError;
	private int price;
	
	public PacketCardsSell(SorciCubeSpell sorci, String name, int price, ConfigurationSection config) {
		this.price = price;
		this.packetCards = sorci.getManagerPacketCards().getPacketCards(name);
		if (this.isValid()) {
			this.itemPacket = sorci.getManagerPacketCards().packetToItem(this.packetCards);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(config.getString("name").replace("{name}", name));
			lore.add(config.getString("price").replace("{price}", Integer.toString(price)));
			this.itemSell = ItemBuild.buildSkull(SkullUrl.SHOP, 1, config.getString("buy"), lore, false);
		} else {
			this.itemError = ItemBuild.buildItem(Material.SIGN, 1, (short) 0, (byte) 0, config.getString("error"), Arrays.asList(name), false);
		}
	}

	public PacketCards getPacketCards() {
		return packetCards;
	}

	public ItemStack getItemPacket() {
		return itemPacket;
	}
	
	public ItemStack getItemError() {
		return itemError;
	}

	public ItemStack getItemSell() {
		return itemSell;
	}

	public int getPrice() {
		return price;
	}

	public boolean isValid() {
		return this.packetCards != null;
	}
	
}
