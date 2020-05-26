package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditCart extends GuiEdit {
	
	private ItemStack back;
	private ItemStack remove;
	private ItemStack deco;
	
	public GuiEditCart(EditCartsGui ec, ConfigurationSection config) {
		super(ec, config);
		this.back = ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("item_name.back"), null, false);
		this.remove = ItemBuild.buildSkull(SkullUrl.TRASH, 1, config.getString("item_name.remove"), null, false);
		this.deco =  ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, config.getString("item_name.deco"), null, false);
	}
	
	@Override
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 36, this.name);
		//deco
		inv.setItem(9, deco);
		inv.setItem(17, deco);
		inv.setItem(18, deco);
		inv.setItem(26, deco);
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, deco);
			inv.setItem(27+i, deco);
		}
		//back
		inv.setItem(38, back);
		//remove
		inv.setItem(42, remove);
		//param
		
		return inv;
	}
	
	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		switch (raw) {
			//back
			case 38:
				
				break;
			//remove
			case 42:
				
				break;
			//default
			default:
				break;
		}
		e.setCancelled(true);
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}

}
