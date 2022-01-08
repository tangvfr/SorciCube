package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiFightGuiConfig;
import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiFight extends AbstractGui<GuiFightGuiConfig> {

	private final ItemStack itemDeco;
	private final ItemStack itemClose;
	private final ItemStack itemNoClassified;
	private final ItemStack itemDuel;
	
	public GuiFight(ManagerGui manager) {
		super(manager, manager.getSorci().config().gui.guiFight);
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 14, " ", null, false);
		itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.close.value, null, false);
		itemNoClassified = ItemBuild.buildSkull(SkullUrl.N_GRAY, 1, config.unclassied.value, null, false);
		itemDuel = ItemBuild.buildItem(Material.IRON_SWORD, 1, (short) 0, (byte) 0, config.duel.value, null, false);
	}

	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, config.name.value);
		inv.setItem(0, itemDeco); inv.setItem(4, itemDeco); inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco); inv.setItem(13, itemDeco); inv.setItem(17, itemDeco);
		inv.setItem(18, itemDeco); inv.setItem(22, itemClose); inv.setItem(26, itemDeco);
		inv.setItem(11, itemNoClassified);
		inv.setItem(15, itemDuel);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
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
			Bukkit.getLogger().throwing("GuiFight" ,"onClick", e2);
		}
	}

}
