package fr.tangv.sorcicubespell.editingcarts;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.util.ItemBuild;

public class GuiEditAnvil extends GuiEdit {

	public GuiEditAnvil(EditCartsGui ec, ConfigurationSection config) {
		super(ec, config);
	}
	
	@Override
	public Inventory getInventory(Player player) {
		AnvilEdit ae = this.ec.editingCarts.get(player).getAnvilEdit();
		if (ae != null) {
			Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, this.name);
			List<String> lore = this.config.getStringList("lore_edit");
			for (int i = 0; i < lore.size(); i++)
				lore.set(i, 
						lore.get(i)
						.replace("{name}", ae.getName())
						.replace("{value}", ae.getValue())
					);
			inv.setItem(0, ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, ae.getName().replace("§", "&"), lore, false));
			return inv;
		} else {
			return player.getInventory();
		}
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		AnvilEdit ae = this.ec.editingCarts.get(player).getAnvilEdit();
		if (ae != null) {
			if (e.getRawSlot() == 2) {
				ItemStack result = e.getCurrentItem();
				if (result.hasItemMeta() && result.getItemMeta().hasDisplayName())
					ae.valid(result.getItemMeta().getDisplayName().replace("&", "§"));
				else
					ae.cancel();
			} else if (e.getRawSlot() == 0) {
				ae.cancel();
			}
		} else {
			player.closeInventory();
		}
		e.setCancelled(true);
	}
	
	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}

}
