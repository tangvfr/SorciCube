package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiEditOrViewGuiConfig;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditOrView extends AbstractGui<GuiEditOrViewGuiConfig> {

	private final ItemStack itemDeco;
	private final ItemStack itemDeck;
	private final ItemStack itemCard;
	private final ItemStack itemClose;
	
	public GuiEditOrView(ManagerGui manager) {
		super(manager, manager.getSorci().config().gui.guiEditOrView);
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, " ", null, false);
		itemDeck = ItemBuild.buildItem(Material.BOOK, 1, (short) 0, (byte) 0, config.deck.value, null, false);
		itemCard = ItemBuild.buildItem(Material.MAP, 1, (short) 0, (byte) 0, config.card.value, null, false);
		itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.close.value, null, false);
	}

	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, config.name.value);
		inv.setItem(0, itemDeco); inv.setItem(4, itemDeco); inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco); inv.setItem(13, itemDeco); inv.setItem(17, itemDeco);
		inv.setItem(18, itemDeco); inv.setItem(22, itemClose); inv.setItem(26, itemDeco);
		inv.setItem(11, itemDeck);
		inv.setItem(15, itemCard);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw == 11) {//deck
			manager.getGuiDecks().open(player);
		} else if (raw == 15) {//card
			manager.getGuiViewCards().open(player);
		} else if (raw == 22) {//close
			player.closeInventory();
		}
	}

}
