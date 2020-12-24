package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubecore.util.RenderException;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiFight extends AbstractGui {

	private ItemStack itemDeco;
	private ItemStack itemClose;
	private ItemStack itemNoClassified;
	private ItemStack itemDuel;
	
	public GuiFight(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_fight"));
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 14, " ", null, false);
		itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false);
		itemNoClassified = ItemBuild.buildSkull(SkullUrl.N_GRAY, 1, config.getString("unclassied"), null, false);
		itemDuel = ItemBuild.buildItem(Material.IRON_SWORD, 1, (short) 0, (byte) 0, config.getString("duel"), null, false);
	}

	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, this.name);
		inv.setItem(0, itemDeco); inv.setItem(4, itemDeco); inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco); inv.setItem(13, itemDeco); inv.setItem(17, itemDeco);
		inv.setItem(18, itemDeco); inv.setItem(22, itemClose); inv.setItem(26, itemDeco);
		inv.setItem(11, itemNoClassified);
		inv.setItem(15, itemDuel);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		try {
			if (raw == 11) {//noclassified
				PlayerGui playerG = getPlayerGui(player);
				playerG.setFightType(FightType.UNCLASSIFIED);
				manager.getGuiFightDeck().open(player);
			} else if (raw == 15) {//duel
				PlayerGui playerG = getPlayerGui(player);
				playerG.setFightType(FightType.DUEL);
				manager.getGuiFightDeck().open(player);
			} else if (raw == 22) {//close
				player.closeInventory();
			}
		} catch (Exception e2) {
			Bukkit.getLogger().warning(RenderException.renderException(e2));
		}
		e.setCancelled(true);
	}

}