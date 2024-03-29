package fr.tangv.sorcicubespell.npc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiTrashGuiConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class TrashNPC extends AbstractGui<GuiTrashGuiConfig> implements ClickNPC {
	
	private ItemStack itemDeco;
	private ItemStack itemDumped;
	
	public TrashNPC(SorciCubeSpell sorci) {
		super(sorci.getManagerGui(), sorci.config().gui.guiTrash);
		this.itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		this.itemDumped = ItemBuild.buildSkull(SkullUrl.TRASH, 1, config.dumped.value, null, false);
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, config.name.value);
		inv.setItem(6, itemDeco);
		inv.setItem(7, itemDumped);
		inv.setItem(8, itemDeco);
		return inv;
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		this.open(player);
	}
	
	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
			e.setCancelled(true);
		else if (raw == 6 || raw == 8 || raw < 0) {
			e.setCancelled(true);
			player.closeInventory();
		} else if (raw == 7) {
			e.setCancelled(true);
			for (int i = 0; i < 6; i++)
				e.getInventory().setItem(i, null);
		}
	}
	
	@Override
	public void onClose(Player player, InventoryCloseEvent e) {
		for (int i = 0; i < 6; i++) {
			ItemStack item = e.getInventory().getItem(i);
			if (item != null)
				player.getInventory().addItem(item);
		}
	}

}
