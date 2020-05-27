package fr.tangv.sorcicubespell.editingcarts;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.util.Gui;

public class EventEditCartsGui implements Listener{

	private EditCartsGui ec;
	
	public EventEditCartsGui(EditCartsGui ec) {
		this.ec = ec;
		for (Player player : Bukkit.getOnlinePlayers())
			this.ec.editingCarts.put(player, new PlayerEditCart(player));
	}
	
	private Map<Player, ItemStack> itemOld = new ConcurrentHashMap<Player, ItemStack>();
	
	private boolean isBookEdit(ItemStack item) {
		if (item.getType() == Material.BOOK_AND_QUILL && item.hasItemMeta()) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			return (meta.hasDisplayName() && meta.hasLore() && meta.getDisplayName().equals("§6§k§r§aEdit Cart"));
		}
		return false;
	}
	
	private String[] getEdit(ItemStack item) {
		if (item.getType() == Material.BOOK_AND_QUILL && item.hasItemMeta()) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			if (meta.hasDisplayName() && meta.hasLore() && meta.getDisplayName().equals("§6§k§r§aEdit Cart")) {
				String text = "";
				for (String page : meta.getPages())
					text += page.replace("\n", "\n§8");
				return new String[] {meta.getLore().get(0), text};
			}
		}
		return null;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.HAND &&
				e.hasItem()) {
			ItemStack item = e.getItem();
			if (isBookEdit(item)) {
				if (e.getPlayer().isSneaking()) {
					String[] sc = getEdit(item);
					Bukkit.broadcastMessage("id: "+sc[0]);
					Bukkit.broadcastMessage("text: "+sc[1]);
					e.getPlayer().getInventory().setItemInMainHand(itemOld.get(e.getPlayer()));
				} else {
					if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
						e.getPlayer().getInventory().setItemInMainHand(itemOld.get(e.getPlayer()));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (isBookEdit(e.getItemDrop().getItemStack())) {
			e.getPlayer().getInventory().setItemInMainHand(itemOld.get(e.getPlayer()));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().equalsIgnoreCase("tangv is the best ever time !")) {
			Player player = e.getPlayer();
			ItemStack book = new ItemStack(Material.BOOK_AND_QUILL, 1);
			BookMeta meta = (BookMeta) book.getItemMeta();
			meta.addPage("Insert your text deal "+player.getName()+":\n");
			meta.setDisplayName("§6§k§r§aEdit Cart");
			meta.setLore(Arrays.asList("proute caca"));
			book.setItemMeta(meta);
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			if (itemOld.containsKey(player))
				itemOld.replace(player, item);
			else
				itemOld.put(player, item);
			player.getInventory().setItemInMainHand(book);
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
			if (isBookEdit(e.getCurrentItem())) {
				e.setCurrentItem(itemOld.get((Player) e.getWhoClicked()));
				e.setCancelled(true);
				return;
			} else if (e.getInventory().getType() == InventoryType.ANVIL) {
				Bukkit.broadcastMessage("Inv: "+e.getInventory().getName());
				ItemStack result = e.getCurrentItem();
				if (result.hasItemMeta() && result.getItemMeta().hasDisplayName())
					Bukkit.broadcastMessage("Text: "+result.getItemMeta().getDisplayName());
			} else {
				PlayerEditCart player = this.ec.editingCarts.get((Player) e.getWhoClicked());
				Gui gui = player.getGui();
				if (gui != null && e.getInventory().getName().equals(gui.getName()))
					gui.onClick(player.getPlayer(), e);
			}
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
