package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.util.BookUtil;
import fr.tangv.sorcicubespell.util.Gui;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

public class EventEditCartsGui implements Listener{

	private EditCartsGui ec;
	
	public EventEditCartsGui(EditCartsGui ec) {
		this.ec = ec;
		for (Player player : Bukkit.getOnlinePlayers())
			this.ec.editingCarts.put(player, new PlayerEditCart(player));
	}
	
	@EventHandler
	public void onBook(PlayerEditBookEvent e) {
		Bukkit.broadcastMessage("edit book");
		BookMeta meta = e.getNewBookMeta();
		Bukkit.broadcastMessage("sign: "+e.isSigning());
		Bukkit.broadcastMessage("text: ");
		for (String s : meta.getPages()) {
			Bukkit.broadcastMessage(s);
		}
	}
	
	@EventHandler
	public void onSneak(AsyncPlayerChatEvent e) {
		if (e.getMessage().equalsIgnoreCase("tangv is the best ever time !")) {
			Player player = e.getPlayer();
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
			BookMeta meta = (BookMeta) book.getItemMeta();
			meta.addPage("Insert your text deal "+player.getName()+":\n");
			book.setItemMeta(meta);
			BookUtil.openBook(book, player);
			
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
			if (e.getInventory().getType() == InventoryType.ANVIL) {
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
