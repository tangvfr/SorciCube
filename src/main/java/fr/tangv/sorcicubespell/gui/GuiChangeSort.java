package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.CardComparator;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiChangeSort extends AbstractGui {

	private ItemStack itemDeco;
	private ItemStack itemBack;
	
	public GuiChangeSort(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_change_sort"));
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		itemBack = ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("back"), null, false);
	}

	private ItemStack itemSort(PlayerGui playerG, String url, CardComparator sort) {
		return ItemBuild.buildSkull(url, playerG.getCardComparator().equals(sort) ? 2 : 1, manager.getSorci().getEnumTool().sortToString(sort), null, false);
	}
	
	@Override
	public Inventory getInventory(Player player) {
		PlayerGui playerG = getPlayerGui(player);
		Inventory inv = Bukkit.createInventory(null, 18, this.name);
		inv.setItem(0, itemDeco);
		inv.setItem(1, itemSort(playerG, SkullUrl.I_DRAY, CardComparator.BY_ID));
		inv.setItem(2, itemSort(playerG, SkullUrl.F_LIME, CardComparator.BY_FACTION));
		inv.setItem(3, itemSort(playerG, SkullUrl.R_YELLOW, CardComparator.BY_RARITY));
		inv.setItem(4, itemSort(playerG, SkullUrl.T_PURPLE, CardComparator.BY_TYPE));
		inv.setItem(5, itemSort(playerG, SkullUrl.DOWN_BLUE, CardComparator.BY_LOW_MANA));
		inv.setItem(6, itemSort(playerG, SkullUrl.UP_BLUE, CardComparator.BY_HIGH_MANA));
		inv.setItem(7, itemSort(playerG, SkullUrl.N_RED, CardComparator.BY_NAME));
		inv.setItem(8, itemDeco);
		for (int i = 9; i < 18; i++)
			inv.setItem(i, itemDeco);
		inv.setItem(13, itemBack);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		PlayerGui playerG = getPlayerGui(player);
		switch (e.getRawSlot()) {
		
			case 1://id
				playerG.setCardComparator(CardComparator.BY_ID);
				break;
				
			case 2://faction
				playerG.setCardComparator(CardComparator.BY_FACTION);
				break;
				
			case 3://rarity
				playerG.setCardComparator(CardComparator.BY_RARITY);
				break;
				
			case 4://type
				playerG.setCardComparator(CardComparator.BY_TYPE);
				break;
				
			case 5://low mana
				playerG.setCardComparator(CardComparator.BY_LOW_MANA);
				break;
				
			case 6://high mana
				playerG.setCardComparator(CardComparator.BY_HIGH_MANA);
				break;
				
			case 7://name
				playerG.setCardComparator(CardComparator.BY_NAME);
				break;
		
			case 13://back
				break;
	
			default:
				return;
				
		}
		playerG.getPreviousGui().open(player);
	}
	
}
