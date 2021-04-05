package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class GuiIncreaseDeck extends AbstractGui {

	private ConfigurationSection priceSection;
	
	public GuiIncreaseDeck(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_increase_deck"));
		this.priceSection = manager.getSorci().getConfigNPC().getConfigurationSection("increase_number_deck_price");
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, this.name);
		String name;
		ArrayList<String> lore = new ArrayList<String>();
		PlayerFeatures feature = getPlayerGui(player).getPlayerFeatures();
		int number = feature.getUnlockDecks()+1;
		if (number <= 5) {
			name = config.getString("unlock").replace("{number}", Integer.toString(number));
			int price = priceSection.getInt(Integer.toString(number));
			if (price >= 0) {
				lore.add(config.getString(feature.getMoney() >= price ? "price_right" : "price_wrong").replace("{price}", Integer.toString(price)));
			} else {
				lore.add(config.getString("prenium"));
			}
		} else {
			name = config.getString("max");
		}
		inv.setItem(4, ItemBuild.buildItem(Material.BOOK, 1, (short) 0, (byte) 0, name, lore, false));
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw >= 0 && raw < 9) {
			if (raw == 4) {
				PlayerGui playerG = getPlayerGui(player);
				PlayerFeatures feature = playerG.getPlayerFeatures();
				int number = feature.getUnlockDecks()+1;
				if (number <= 5) {
					int price = priceSection.getInt(Integer.toString(number));
					if (price >= 0) {
						if (feature.getMoney() >= price) {
							feature.removeMoney(price);
							feature.setUnlockDecks(number);
							playerG.uploadPlayerFeatures(manager.getSorci().getHandlerPlayers());
							player.sendMessage(getMessage("message_increase_deck_unlock")
									.replace("{number}", Integer.toString(number))
									.replace("{price}", Integer.toString(price))
							);
							this.open(player);
						} else { 
							player.sendMessage(getMessage("message_increase_deck_no_money"));
							player.closeInventory();
						}
					} else {
						player.sendMessage(getMessage("message_increase_deck_prenium"));
					}
				} else {
					player.sendMessage(getMessage("message_increase_deck_max"));
				}
			} else {
				player.closeInventory();
			}
		}
	}

}
