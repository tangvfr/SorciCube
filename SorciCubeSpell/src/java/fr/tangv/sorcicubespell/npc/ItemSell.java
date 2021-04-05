package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class ItemSell {

	private final ItemStack item;
	private final String itemName;
	private ItemStack itemShopRight;
	private ItemStack itemShopWrong;
	private final int price;
	
	private ItemStack duplicateItemShopLore(SorciCubeSpell sorci, String key) {
		ArrayList<String> lore = new ArrayList<String>();
		for (String line : sorci.getGuiConfig().getStringList("gui_seller_items."+key))
			lore.add(line.replace("{price}", Integer.toString(price)));
		ItemStack item = this.item.clone();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemSell(SorciCubeSpell sorci, String data) throws Exception {
		try {
			String[] datas = data.split(":");
			this.item = (datas[0].equalsIgnoreCase("none")) 
					? ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false)
					: sorci.getConfigItemList().getItemStack(datas[0]);
			this.price = Integer.parseInt(datas[1]);
			this.itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
					item.getItemMeta().getDisplayName() : item.getData().getItemType().name();
			if (price > 0) {
				this.itemShopRight = duplicateItemShopLore(sorci, "lore_right");
				this.itemShopWrong = duplicateItemShopLore(sorci, "lore_wrong");
			}
		} catch (Exception e) {
			throw new Exception("ItemSell Invalid Data");
		}
	}
	
	public String getName() {
		return itemName;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public boolean isDeco() {
		return price < 0;
	}
	
	public ItemStack getItemShop(PlayerGui player) {
		if (isDeco())
			return item;
		else
			return hasMoney(player) ? itemShopRight : itemShopWrong;
	}
	
	public boolean hasMoney(PlayerGui player) {
		return player.getPlayerFeatures().hasMoney(price);
	}
	
	public int getPrice() {
		return price;
	}
	
}
