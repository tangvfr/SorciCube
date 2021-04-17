package fr.tangv.sorcicubespell.npc;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubecore.configs.GuiSellerItemsGuiConfig;
import fr.tangv.sorcicubecore.configs.npc.ItemSellerItemsNPCConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class ItemSell {

	private final boolean isDeco;
	private final ItemStack item;
	private final String itemName;
	private final ItemStack itemShopRight;
	private final ItemStack itemShopWrong;
	private final int price;
	
	private ItemStack duplicateItemShopLore(GuiSellerItemsGuiConfig conf, boolean right) {
		ArrayList<String> lore = new ArrayList<String>();
		for (String line : (right ? conf.loreRight : conf.loreWrong).toArrayString())
			lore.add(line.replace("{price}", Integer.toString(price)));
		ItemStack item = this.item.clone();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemSell(ItemSellerItemsNPCConfig conf, GuiSellerItemsGuiConfig gui) throws Exception {
		try {
			this.isDeco = !conf.isEnable.value;
			if (isDeco) {
				this.item = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
				this.itemShopRight = null;
				this.itemShopWrong = null;
				this.price = -1;
			} else {
				this.item = /*sorci.get conf.nameItem*/;
				this.itemShopRight = duplicateItemShopLore(gui, true);
				this.itemShopWrong = duplicateItemShopLore(gui, false);
				this.price = conf.price.value;
			}
			this.itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
					item.getItemMeta().getDisplayName() : item.getData().getItemType().name();
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
		return isDeco;
	}
	
	public ItemStack getItemShop(PlayerGui player) {
		if (isDeco)
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
