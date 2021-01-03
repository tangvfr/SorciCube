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

public class SellerItemsNPC extends AbstractGui implements ClickNPC {

	private String nameNPC;
	private ItemSell[] items;
	
	public SellerItemsNPC(SorciCubeSpell sorci, String name) throws Exception {
		super(sorci.getManagerGui(), sorci.getGuiConfig().getConfigurationSection("gui_seller_items"));
		ConfigurationSection config = sorci.getConfigNPC().getConfigurationSection("npc_seller_item."+name);
		this.nameNPC = config.getString("name_npc");
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
		updateInventory(inv, getPlayerGui(player));
		return inv;
	}
	
	private void updateInventory(Inventory inv, PlayerGui player) {
		for (int i = 0; i < 9; i++)
			inv.setItem(i, items[i].getItemShop(player));
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw >= 0 && raw < 9) {
			ItemSell item = items[raw];
			if (!item.isDeco()) {
				PlayerGui playerG = getPlayerGui(player);
				if (item.hasMoney(playerG)) {
					playerG.getPlayerFeature().removeMoney(item.getPrice());
					playerG.uploadPlayerFeature(manager.getSorci().getHandlerPlayers());
					player.getInventory().addItem(item.getItem());
					player.sendMessage(getMessage("message_seller_items_buy")
							.replace("{name}", item.getName())
							.replace("{price}", Integer.toString(item.getPrice()))
					);
					updateInventory(e.getInventory(), playerG);
				} else { 
					player.sendMessage(getMessage("message_seller_items_no_money"));
					player.closeInventory();
				}
			} else {
				player.closeInventory();
			}
		}
	}

}
