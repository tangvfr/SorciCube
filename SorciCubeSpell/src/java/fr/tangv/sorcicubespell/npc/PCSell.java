package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiSellerPacketsGuiConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public abstract class PCSell {

	protected final SorciCubeSpell sorci;
	protected final int price;
	protected ItemStack itemView;
	protected String nameItemSell;
	protected ArrayList<String> loreRight;
	protected ArrayList<String> loreWrong;
	
	public PCSell(SorciCubeSpell sorci, int price) {
		this.sorci = sorci;
		this.price = price;
	}
	
	public void initItemSell(GuiSellerPacketsGuiConfig conf, boolean isPacket, String name) {
		if (isPacket) {
			this.nameItemSell = conf.packetBuy.value;
			this.loreRight = new ArrayList<String>();
			this.loreRight.add(conf.packetNameRight.value.replace("{name}", name));
			this.loreRight.add(conf.packetPriceRight.value.replace("{price}", Integer.toString(price)));
			this.loreWrong = new ArrayList<String>();
			this.loreWrong.add(conf.packetNameWrong.value.replace("{name}", name));
			this.loreWrong.add(conf.packetPriceWrong.value.replace("{price}", Integer.toString(price)));
		} else {
			this.nameItemSell = conf.cardBuy.value;
			this.loreRight = new ArrayList<String>();
			this.loreRight.add(conf.cardNameRight.value.replace("{name}", name));
			this.loreRight.add(conf.cardPriceRight.value.replace("{price}", Integer.toString(price)));
			this.loreWrong = new ArrayList<String>();
			this.loreWrong.add(conf.cardNameWrong.value.replace("{name}", name));
			this.loreWrong.add(conf.cardPriceWrong.value.replace("{price}", Integer.toString(price)));
		}
	}
	
	public abstract boolean buy(PlayerGui player);
	public abstract boolean isValid();
	
	public ItemStack getItemSell(PlayerGui player) {
		return ItemBuild.buildSkull(SkullUrl.SHOP, 1, nameItemSell, (hasMoney(player) ? loreRight : loreWrong), false);
	}
	
	public boolean hasMoney(PlayerGui player) {
		return player.getPlayerFeatures().hasMoney(price);
	}
	
	public ItemStack getItemView() {
		return itemView;
	}
	
	public int getPrice() {
		return price;
	}
	
}
