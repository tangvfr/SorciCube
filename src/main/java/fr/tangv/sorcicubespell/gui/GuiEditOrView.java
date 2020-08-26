package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditOrView extends AbstractGui {

	private ItemStack itemDeco;
	private ItemStack itemDeck;
	private ItemStack itemCard;
	private ItemStack itemClose;
	
	public GuiEditOrView(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_edit_or_view"));
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, " ", null, false);
		itemDeck = ItemBuild.buildItem(Material.BOOK, 1, (short) 0, (byte) 0, config.getString("deck"), null, false);
		itemCard = ItemBuild.buildItem(Material.MAP, 1, (short) 0, (byte) 0, config.getString("card"), null, false);
		itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false);
	}

	@Override
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, this.name);
		inv.setItem(0, itemDeco); inv.setItem(4, itemDeco); inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco); inv.setItem(13, itemDeco); inv.setItem(17, itemDeco);
		inv.setItem(18, itemDeco); inv.setItem(22, itemClose); inv.setItem(26, itemDeco);
		inv.setItem(11, itemDeck);
		inv.setItem(15, itemCard);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (raw == 11) {//deck
			manager.getGuiDecks().open(player);
		} else if (raw == 15) {//card
			manager.getGuiViewCards().open(player);
		} else if (raw == 22) {//close
			player.closeInventory();
		}
		e.setCancelled(true);
	}

}
