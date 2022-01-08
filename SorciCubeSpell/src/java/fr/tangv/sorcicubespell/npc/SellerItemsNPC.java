package fr.tangv.sorcicubespell.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubecore.configs.GuiSellerItemsGuiConfig;
import fr.tangv.sorcicubecore.configs.npc.SellerItemsNPCConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class SellerItemsNPC extends AbstractGui<GuiSellerItemsGuiConfig> implements ClickNPC {

	private final String nameNPC;
	private final ItemSell[] items;
	
	public SellerItemsNPC(SorciCubeSpell sorci, SellerItemsNPCConfig conf) throws Exception {
		super(sorci.getManagerGui(), sorci.config().gui.guiSellerItems);
		this.nameNPC = conf.nameNPC.value;
		this.items = new ItemSell[9];
		this.items[0] = new ItemSell(conf.item1, config, sorci.getItems());
		this.items[1] = new ItemSell(conf.item2, config, sorci.getItems());
		this.items[2] = new ItemSell(conf.item3, config, sorci.getItems());
		this.items[3] = new ItemSell(conf.item4, config, sorci.getItems());
		this.items[4] = new ItemSell(conf.item5, config, sorci.getItems());
		this.items[5] = new ItemSell(conf.item6, config, sorci.getItems());
		this.items[6] = new ItemSell(conf.item7, config, sorci.getItems());
		this.items[7] = new ItemSell(conf.item8, config, sorci.getItems());
		this.items[8] = new ItemSell(conf.item9, config, sorci.getItems());
	}
	
	public String getNameNPC() {
		return nameNPC;
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (getPlayerGui(player).getPlayerFeatures() != null)
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
					playerG.getPlayerFeatures().removeMoney(item.getPrice());
					playerG.uploadPlayerFeatures(manager.getSorci().getHandlerPlayers());
					player.getInventory().addItem(item.getItem());
					player.sendMessage(sorci.config().messages.sellerItemsBuy.value
							.replace("{name}", item.getName())
							.replace("{price}", Integer.toString(item.getPrice()))
					);
					updateInventory(e.getInventory(), playerG);
				} else { 
					player.sendMessage(sorci.config().messages.sellerItemsNoMoney.value);
					player.closeInventory();
				}
			} else {
				player.closeInventory();
			}
		}
	}

}
