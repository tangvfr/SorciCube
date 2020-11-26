package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public abstract class PCSell {

	protected final SorciCubeSpell sorci;
	protected final ConfigurationSection config;
	protected final int price;
	protected ItemStack itemView;
	protected String nameItemSell;
	protected ArrayList<String> loreRight;
	protected ArrayList<String> loreWrong;
	
	public PCSell(SorciCubeSpell sorci, ConfigurationSection config, int price) {
		this.sorci = sorci;
		this.config = config;
		this.price = price;
	}
	
	public void initItemSell(String key, String name) {
		this.nameItemSell = config.getString(key+"_buy");
		this.loreRight = new ArrayList<String>();
		this.loreRight.add(config.getString(key+"_name_right").replace("{name}", name));
		this.loreRight.add(config.getString(key+"_price_right").replace("{price}", Integer.toString(price)));
		this.loreWrong = new ArrayList<String>();
		this.loreWrong.add(config.getString(key+"_name_wrong").replace("{name}", name));
		this.loreWrong.add(config.getString(key+"_price_wrong").replace("{price}", Integer.toString(price)));
	}
	
	public abstract boolean buy(PlayerGui player);
	public abstract boolean isValid();
	
	public ItemStack getItemSell(PlayerGui player) {
		return ItemBuild.buildSkull(SkullUrl.SHOP, 1, nameItemSell, (hasMoney(player) ? loreRight : loreWrong), false);
	}
	
	public boolean hasMoney(PlayerGui player) {
		return player.getPlayerFeature().hasMoney(price);
	}
	
	public ItemStack getItemView() {
		return itemView;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getMessage(String key) {
		return sorci.getMessage().getString(key);
	}
	
}
