package fr.tangv.sorcicubespell.editingcarts;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.carts.CartRender;
import fr.tangv.sorcicubespell.util.ItemBuild;

public abstract class GuiEditConfirm extends GuiEdit {

	private ItemStack valid;
	private ItemStack cancel;
	private ItemStack deco;
	private String messageConfirm;
	
	public GuiEditConfirm(EditCartsGui ec, String nameConfirm) {
		super(ec, ec.sorci.getGui().getConfigurationSection("gui_edit_confirm"));
		this.messageConfirm = this.config.getString("confirm."+nameConfirm);
		this.valid =  ItemBuild.buildItem(Material.STAINED_CLAY, 1, (short) 0, (byte) 5, this.config.getString("valid"), null, false);
		this.cancel = ItemBuild.buildItem(Material.STAINED_CLAY, 1, (short) 0, (byte) 14, this.config.getString("cancel"), null, false);
		this.deco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, this.config.getString("deco"), null, false);
	}
	
	protected abstract void valid(PlayerEditCart player);
	protected abstract void cancel(PlayerEditCart player);
	
	@Override
	public Inventory getInventory(Player player) {
		PlayerEditCart playerE = this.ec.editingCarts.get(player);
		Inventory inv = Bukkit.createInventory(null, 9, this.name);
		//valid
		ItemStack valid = this.valid.clone();
		ItemMeta metaValid = valid.getItemMeta();
		metaValid.setLore(Arrays.asList(this.messageConfirm));
		valid.setItemMeta(metaValid);
		inv.setItem(1, valid);
		//deco
		inv.setItem(3, deco);
		//cart
		ItemStack item = CartRender.cartToItem(playerE.getCart(), this.ec.sorci);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(this.messageConfirm);
		item.setItemMeta(metaItem);
		inv.setItem(4, item);
		//deco
		inv.setItem(5, deco);
		//cancel
		ItemStack cancel = this.cancel.clone();
		ItemMeta metaCancel = cancel.getItemMeta();
		metaCancel.setLore(Arrays.asList(this.messageConfirm));
		cancel.setItemMeta(metaCancel);
		inv.setItem(7, cancel);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		PlayerEditCart playerE = this.ec.editingCarts.get(player);
		int raw = e.getRawSlot();
		switch (raw) {
			case 1:
				this.valid(playerE);
				break;
	
			case 7:
				this.cancel(playerE);
				break;
				
			default:
				break;
		}
		e.setCancelled(true);
	}
	

	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}

}
