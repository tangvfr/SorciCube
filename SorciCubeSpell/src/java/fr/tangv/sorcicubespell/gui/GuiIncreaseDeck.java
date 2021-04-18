package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.configs.GuiIncreaseDeckGuiConfig;
import fr.tangv.sorcicubecore.configs.npc.DeckPriceNPCConfig;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class GuiIncreaseDeck extends AbstractGui<GuiIncreaseDeckGuiConfig> {

	private final DeckPriceNPCConfig prices;
	
	public GuiIncreaseDeck(ManagerGui manager) {
		super(manager, manager.getSorci().config().gui.guiIncreaseDeck);
		this.prices = manager.getSorci().config().npc.increaseNumberDeckPrice;
	}
	
	private int getPrice(int deck) {
		switch (deck) {
			case 1:
				return prices.deck1.value;
	
			case 2:
				return prices.deck2.value;
				
			case 3:
				return prices.deck3.value;
				
			case 4:
				return prices.deck4.value;
				
			case 5:
				return prices.deck5.value;
				
			default:
				return 0;
		}
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, config.name.value);
		String name;
		ArrayList<String> lore = new ArrayList<String>();
		PlayerFeatures feature = getPlayerGui(player).getPlayerFeatures();
		int number = feature.getUnlockDecks()+1;
		if (number <= 5) {
			name = config.unlock.value.replace("{number}", Integer.toString(number));
			int price = getPrice(number);
			if (price >= 0) {
				lore.add((feature.getMoney() >= price ? config.priceRight : config.priceWrong).value.replace("{price}", Integer.toString(price)));
			} else {
				lore.add(config.prenium.value);
			}
		} else {
			name = config.max.value;
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
					int price = getPrice(number);
					if (price >= 0) {
						if (feature.getMoney() >= price) {
							feature.removeMoney(price);
							feature.setUnlockDecks(number);
							playerG.uploadPlayerFeatures(manager.getSorci().getHandlerPlayers());
							player.sendMessage(manager.getSorci().config().messages.increaseDeckUnlock.value
									.replace("{number}", Integer.toString(number))
									.replace("{price}", Integer.toString(price))
							);
							this.open(player);
						} else { 
							player.sendMessage(manager.getSorci().config().messages.increaseDeckNoMoney.value);
							player.closeInventory();
						}
					} else {
						player.sendMessage(manager.getSorci().config().messages.increaseDeckPrenium.value);
					}
				} else {
					player.sendMessage(manager.getSorci().config().messages.increaseDeckMax.value);
				}
			} else {
				player.closeInventory();
			}
		}
	}

}
