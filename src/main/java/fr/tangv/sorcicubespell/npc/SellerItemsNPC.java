package fr.tangv.sorcicubespell.npc;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.player.PlayerFeature;

public class SellerItemsNPC extends AbstractGui implements ClickNPC {

	private String nameNPC;
	private ItemSell[] items;
	
	public SellerItemsNPC(SorciCubeSpell sorci, String name) throws Exception {
		super(sorci.getManagerGui(), sorci.getGuiConfig().getConfigurationSection("gui_seller_items"));
		ConfigurationSection config = sorci.getConfigNPC().getConfigurationSection("npc_seller_item."+name);
		this.nameNPC = config.getString("name");
		List<String> items_selled = config.getStringList("items_selled");
		if (items_selled.size() != 9)
			throw new Exception("List \"item_selled\" of \""+name+"\" is invalid !");
		this.items = new ItemSell[9];
		for (int i = 0; i < 9; i++)
			this.items[i] = new ItemSell(sorci, items_selled.get(i));
	}
	
	public String getNameNPC() {
		return nameNPC;
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (getPlayerGui(player).getPlayerFeature() != null)
			this.open(player);
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, nameNPC);
		for (int i = 0; i < 9; i++)
			inv.setItem(i, items[i].itemShop);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (raw >= 0 && raw < 9) {
			int price = items[raw].price;
			if (price > 0) {
				PlayerGui playerG = getPlayerGui(player);
				PlayerFeature feature = playerG.getPlayerFeature();
				if (feature.getMoney() >= price) {
					feature.removeMoney(price);
					playerG.uploadPlayerFeature(manager.getSorci().getManagerPlayers());
					player.getInventory().addItem(items[raw].item);
					player.sendMessage(getMessage("message_seller_items_buy")
							.replace("{name}", items[raw].itemName)
							.replace("{price}", Integer.toString(price))
					);
				} else { 
					player.sendMessage(getMessage("message_seller_items_no_money"));
					player.closeInventory();
				}
			} else {
				player.closeInventory();
			}
		} else {
			player.closeInventory();
		}
	}

}
