package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.util.Gui;

public class EventEditCartsGui implements Listener{

	private EditCartsGui ec;
	
	public EventEditCartsGui(EditCartsGui ec) {
		this.ec = ec;
		for (Player player : Bukkit.getOnlinePlayers())
			this.ec.editingCarts.put(player, new PlayerEditCart(player));
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.hasItem() && e.getHand() == EquipmentSlot.HAND &&
				(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
			ItemStack item = e.getItem();
			String[] sc = this.ec.getEditBook(item);
			if (sc != null) {
				Player player = e.getPlayer();
				if (player.hasPermission(ec.sorci.getParameter().getString("permission.editdesc"))) {
					Cart cart = this.ec.setDescCartByBook(sc[0], sc[1]);
					PlayerEditCart p = this.ec.editingCarts.get(player);
					p.setCart(cart);
					this.ec.guiBooks.get("editcart").open(p, cart);
				} else {
					player.sendMessage(ec.sorci.getMessage().getString("message_no_perm_editdesc"));
				}
				player.getInventory().setItemInMainHand(null);
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.ec.editingCarts.put(e.getPlayer(), new PlayerEditCart(e.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.ec.editingCarts.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getWhoClicked());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onDrag(player.getPlayer(), e);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getWhoClicked());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onClick(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getPlayer());
			Gui gui = player.getGui();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onClose(player.getPlayer(), e);
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			PlayerEditCart player = this.ec.editingCarts.get((Player) e.getPlayer());
			Gui gui = player.getGuiOpened();
			if (gui != null && e.getInventory().getName().equals(gui.getName()))
				gui.onOpen(player.getPlayer(), e);
		}
	}
	
}
