package fr.tangv.sorcicubespell.editingcarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartComparator;
import fr.tangv.sorcicubespell.carts.CartRender;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditList extends GuiEdit {

	private ItemStack sort;
	private ItemStack previous;
	private ItemStack next;
	private ItemStack close;
	private ItemStack deco;
	private ItemStack createSort;
	private ItemStack createEntity;
	
	public GuiEditList(EditCartsGui ec) {
		super(ec, ec.sorci.getGui().getConfigurationSection("gui_edit_list"));
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
		return this.getInventory(player, this.ec.editingCarts.get(player).getPageEditGui());
	}
	
	public Inventory getInventory(Player player, int page) {
		PlayerEditCart playerE = this.ec.editingCarts.get(player);
		Inventory inv = Bukkit.createInventory(null, 54, this.name);
		ArrayList<Cart> carts = new ArrayList<Cart>(ec.sorci.getCarts().getCarts());
		CartComparator sorted = playerE.getCartComparator();
		carts.sort(CartComparator.BY_ID);
		carts.sort(sorted);
		//define max page
		int max = carts.size() < 1 ? 0 : (carts.size()-1)/45;
		page = page > max ? max : (page < 0 ? 0 : page);
		playerE.setPageEditGui(page);
		//set carts item
		int decal = page*45;
		int num = carts.size()-decal;
		if (num > 45)
			num = 45;
		if (carts.size() > 0)
			for (int i = 0; i < num; i++) {
				inv.setItem(i, CartRender.cartToItem(carts.get(i+decal), this.ec.sorci));
			}
		//init paper
		ItemStack pageItem = ItemBuild.buildItem(Material.PAPER, page+1, (short) 0, (byte) 0, 
				config.getString("item_name.page")
					.replace("{page}", ""+(page+1))
					.replace("{max}", ""+(max+1))
				, null, false);
		//set tool bar
		ItemStack sortItem = this.sort.clone();
		ItemMeta sortMeta = sortItem.getItemMeta();
		sortMeta.setLore(Arrays.asList(this.ec.sorci.getEnumTool().sortToString(sorted)));
		sortItem.setItemMeta(sortMeta);
		inv.setItem(45, sortItem);
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
			ItemStack item = e.getCurrentItem();
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				List<String> lore = item.getItemMeta().getLore();
				if (lore.size() > 0) {
					String id = lore.get(lore.size()-1).replaceFirst("§8Id: ", "");
					Cart cart = this.ec.sorci.getCarts().getCart(id);
					if (cart != null) {
						PlayerEditCart p = this.ec.editingCarts.get(player);
						p.setCart(cart);
						this.ec.guiBooks.get(BookGuis.MAIN).open(p, cart);
					}
				}
			}
		} else {
			int page = e.getInventory().getItem(49).getAmount();
			switch (raw) {
				//previous
				case 45:
					this.ec.guiBooksEnum.get("sort").open(this.ec.editingCarts.get(player));
					break;
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
					ec.sorci.getCarts().newCartSort();
					player.openInventory(this.getInventory(player, page-1));
					break;
				//create entity
				case 52:
					ec.sorci.getCarts().newCartEntity();
					player.openInventory(this.getInventory(player, page-1));
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
