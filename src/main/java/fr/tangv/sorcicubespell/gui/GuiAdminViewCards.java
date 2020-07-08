package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardComparator;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiAdminViewCards extends AbstractGui {

	private ItemStack sort;
	private ItemStack previous;
	private ItemStack next;
	private ItemStack close;
	private ItemStack deco;
	
	public GuiAdminViewCards(ManagerGui manager) {
		super(manager, manager.getSorci().gertGuiConfig().getConfigurationSection("gui_admin_view_cards"));
		this.sort = ItemBuild.buildSkull(SkullUrl.HOPPER, 1, config.getString("item_name.sort"), null, false);
		this.previous = ItemBuild.buildSkull(SkullUrl.BACK_GRAY, 1, config.getString("item_name.previous"), null, false);
		this.next = ItemBuild.buildSkull(SkullUrl.FORWARD_GRAY, 1, config.getString("item_name.next"), null, false);
		this.close = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("item_name.close"), null, false);
		this.deco =  ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, " ", null, false);
	}

	@Override
	public Inventory getInventory(Player player) {
		PlayerGui playerG = getPlayerGui(player);
		int page = playerG.getPageViewGui();
		HashMap<UUID, Card> hashCards = manager.getSorci().getManagerCards().getCarts();
		ArrayList<Card> cards = new ArrayList<Card>(hashCards.values());
		CardComparator sorted = playerG.getCardComparator();
		cards.sort(CardComparator.BY_ID);
		cards.sort(sorted);
		//define max page
		int max = cards.size() < 1 ? 0 : (cards.size()-1)/45;
		page = page > max ? max : (page < 0 ? 0 : page);
		playerG.setPageViewGui(page);
		//set carts item
		int decal = page*45;
		int num = cards.size()-decal;
		if (num > 45)
			num = 45;
		//init inv
		Inventory inv = Bukkit.createInventory(null, 54, 
				this.name
				.replace("{page}", ""+(page+1))
				.replace("{max}", ""+(max+1)));
		//set inv
		if (cards.size() > 0)
			for (int i = 0; i < num; i++) {
				inv.setItem(i, CardRender.cardToItem(cards.get(i+decal), this.manager.getSorci(), hashCards));
			}
		//init paper
		ItemStack pageItem = ItemBuild.buildItem(Material.PAPER, page+1, (short) 0, (byte) 0, 
				config.getString("item_name.page")
					.replace("{page}", ""+(page+1))
					.replace("{max}", ""+(max+1))
				, null, false);
		//set tool bar
		ItemStack sortItem = this.sort.clone();
		ItemMeta sortMeta = sortItem.getItemMeta();
		sortMeta.setLore(Arrays.asList(this.manager.getSorci().getEnumTool().sortToString(sorted)));
		sortItem.setItemMeta(sortMeta);
		inv.setItem(45, sortItem);
		inv.setItem(46, this.deco);
		inv.setItem(47, this.deco);
		inv.setItem(48, this.previous);
		inv.setItem(49, pageItem);
		inv.setItem(50, this.next);
		inv.setItem(51, this.deco);
		inv.setItem(52, this.deco);
		inv.setItem(53, this.close);
		return inv;
	}
	
	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (raw >= 45) {
			PlayerGui playerG = getPlayerGui(player);
			switch (raw) {
				//previous
				case 45:
					playerG.setPreviousGui(this);
					manager.getGuiChangeSort().open(player);
					break;
				//previous
				case 48:
					playerG.setPageViewGui(playerG.getPageViewGui()-1);
					this.open(player);
					break;
				//page
				case 49:
					this.open(player);
					break;
				//next
				case 50:
					playerG.setPageViewGui(playerG.getPageViewGui()+1);
					this.open(player);
					break;
				//close
				case 53:
					player.closeInventory();
					break;
				//default
				default:
					break;
			}
		}
		e.setCancelled(true);
	}
	
}
