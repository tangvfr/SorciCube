package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class ItemSell {

	protected final ItemStack item;
	protected final ItemStack itemShop;
	protected final String itemName;
	protected final int price;
	
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
				ArrayList<String> lore = new ArrayList<String>();
				for (String line : sorci.getGuiConfig().getStringList("gui_seller_items.lore"))
					lore.add(line.replace("{price}", Integer.toString(price)));
				this.itemShop = item.clone();
				ItemMeta meta = this.itemShop.getItemMeta();
				meta.setLore(lore);
				this.itemShop.setItemMeta(meta);
			} else {
				this.itemShop = item;
			}
		} catch (Exception e) {
			throw new Exception("ItemSell Invalid Data");
		}
	}
	
}
