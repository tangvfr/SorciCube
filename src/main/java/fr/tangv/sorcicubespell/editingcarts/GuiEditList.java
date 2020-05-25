package fr.tangv.sorcicubespell.editingcarts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditList extends GuiEdit {

	private ConfigurationSection config;
	private ItemStack sort;
	private ItemStack previous;
	private ItemStack next;
	private ItemStack close;
	private ItemStack deco;
	private ItemStack createSort;
	private ItemStack createEntity;
	
	public GuiEditList(EditCartsGui ec, ConfigurationSection config) {
		super(ec, config.getString("name"));
		this.config = config;
		this.sort = ItemBuild.buildSkull(SkullUrl.HOPPER, 1, config.getString("item_name.sort"), null, false);
		this.previous = ItemBuild.buildSkull(SkullUrl.BACK_GRAY, 1, config.getString("item_name.previous"), null, false);
		this.next = ItemBuild.buildSkull(SkullUrl.FORWARD_GRAY, 1, config.getString("item_name.next"), null, false);
		this.close = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("item_name.close"), null, false);
		this.deco =  ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, config.getString("item_name.deco"), null, false);
		this.createSort = ItemBuild.buildItem(Material.BLAZE_POWDER, 1, (short) 0, (byte) 0, config.getString("item_name.create_sort"), null, false);
		this.createEntity = ItemBuild.buildItem(Material.ROTTEN_FLESH, 1, (short) 0, (byte) 0, config.getString("item_name.create_entity"), null, false);
	}

	@Override
	public Inventory getInventory(Player player) {
		return this.getInventory(player, 0);
	}
	
	public Inventory getInventory(Player player, int page) {
		Inventory inv = Bukkit.createInventory(null, 54, this.name);
		ArrayList<Cart> carts = new ArrayList<Cart>(ec.sorci.getCarts().getCarts());
		int max = carts.size() < 1 ? 0 : carts.size()-1/45;
		page = page > max ? max : (page < 0 ? 0 : page);
		//set carts item
		int decal = page*45;
		if (carts.size() > 0)
			for (int i = 0; i < 45; i++) {
				inv.setItem(i, carts.get(i+decal).toItem());
			}
		//init paper
		ItemStack pageItem = ItemBuild.buildItem(Material.PAPER, page+1, (short) 0, (byte) 0, 
				config.getString("item_name.page")
					.replace("{page}", ""+(page+1))
					.replace("{max}", ""+(max+1))
				, null, false);
		//set tool bar
		inv.setItem(45, this.sort);
		inv.setItem(46, this.deco);
		inv.setItem(47, this.deco);
		inv.setItem(48, this.previous);
		inv.setItem(49, pageItem);
		inv.setItem(50, this.next);
		inv.setItem(51, this.createSort);
		inv.setItem(52, this.createEntity);
		inv.setItem(53, this.close);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (raw < 45) {
			
		} else {
			int page = e.getInventory().getItem(49).getAmount();
			switch (raw) {
				//previous
				case 48:
					player.openInventory(this.getInventory(player, page-2));
					break;
				//page
				case 49:
					player.openInventory(this.getInventory(player, page-1));
					break;
				//next
				case 50:
					player.openInventory(this.getInventory(player, page));
					break;
				//create sort
				case 51:
					
					break;
				//create entity
				case 52:
					
					break;
				//close
				case 53:
					player.closeInventory();
					break;
				//default
				default:
					break;
			}
		}
		e.setCancelled(true);
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}
	
}
