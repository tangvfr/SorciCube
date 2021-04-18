package fr.tangv.sorcicubespell.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.configs.GuiSelectDeckDefaultGuiConfig;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiSelectDefaultDeck extends AbstractGui<GuiSelectDeckDefaultGuiConfig> {

	private final ItemStack itemDark;
	private final ItemStack itemLight;
	private final ItemStack itemNature;
	private final ItemStack itemToxic;
	private final ItemStack itemDeco;
	
	public GuiSelectDefaultDeck(ManagerGui manager) {
		super(manager, manager.getSorci().config().gui.guiSelectDeckDefault);
		String desc = config.loreSelect.value;
		String dark = manager.getSorci().getEnumTool().factionToString(CardFaction.DARK);
		String light = manager.getSorci().getEnumTool().factionToString(CardFaction.LIGHT);
		String nature = manager.getSorci().getEnumTool().factionToString(CardFaction.NATURE);
		String toxic = manager.getSorci().getEnumTool().factionToString(CardFaction.TOXIC);
		itemDark = ItemBuild.buildSkull(SkullUrl.ENDER_ORB, 1, dark, Arrays.asList(desc.replace("{faction}", dark)), false);
		itemLight = ItemBuild.buildSkull(SkullUrl.BALLOON_YELLOW, 1, light, Arrays.asList(desc.replace("{faction}", light)), false);
		itemNature = ItemBuild.buildSkull(SkullUrl.LECTTUCE, 1, nature, Arrays.asList(desc.replace("{faction}", nature)), false);
		itemToxic = ItemBuild.buildSkull(SkullUrl.BLACK_BERRY, 1, toxic, Arrays.asList(desc.replace("{faction}", toxic)), false);
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
	}

	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 45, config.name.value);
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, itemDeco);
			inv.setItem(i+36, itemDeco);
		}
		inv.setItem(19, itemDark);
		inv.setItem(21, itemLight);
		inv.setItem(23, itemNature);
		inv.setItem(25, itemToxic);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		CardFaction faction;
		switch (raw) {
		
			case 19://dark
				faction = CardFaction.DARK;
				break;
	
			case 21://light
				faction = CardFaction.LIGHT;
				break;
				
			case 23://nature
				faction = CardFaction.NATURE;
				break;
				
			case 25://toxic
				faction = CardFaction.TOXIC;
				break;
				
			default:
				return;
		}
		PlayerFeatures pf = null;
		try {
			pf = sorci.getHandlerPlayers().initPlayer(player.getUniqueId(), player.getName(), faction);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (pf != null) {
			getPlayerGui(player).setPlayerFeatures(pf, sorci);
			player.sendMessage(sorci.config().messages.selectDefaultDeck.value
					.replace("{faction}", sorci.getEnumTool().factionToString(faction)));
		} else
			player.sendMessage(sorci.config().messages.alreadySelectDefaultDeck.value);
		player.closeInventory();
	}

}
