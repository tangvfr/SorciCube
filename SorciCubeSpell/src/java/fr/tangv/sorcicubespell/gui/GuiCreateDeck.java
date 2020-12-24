package fr.tangv.sorcicubespell.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiCreateDeck extends AbstractGui {

	private ItemStack itemDark;
	private ItemStack itemLight;
	private ItemStack itemNature;
	private ItemStack itemToxic;
	private ItemStack itemDeco;
	private ItemStack itemBack;
	
	public GuiCreateDeck(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_create_deck"));
		String desc = config.getString("lore_select");
		String dark = manager.getSorci().getEnumTool().factionToString(CardFaction.DARK);
		String light = manager.getSorci().getEnumTool().factionToString(CardFaction.LIGHT);
		String nature = manager.getSorci().getEnumTool().factionToString(CardFaction.NATURE);
		String toxic = manager.getSorci().getEnumTool().factionToString(CardFaction.TOXIC);
		itemDark = ItemBuild.buildSkull(SkullUrl.ENDER_ORB, 1, dark, Arrays.asList(desc.replace("{faction}", dark)), false);
		itemLight = ItemBuild.buildSkull(SkullUrl.BALLOON_YELLOW, 1, light, Arrays.asList(desc.replace("{faction}", light)), false);
		itemNature = ItemBuild.buildSkull(SkullUrl.LECTTUCE, 1, nature, Arrays.asList(desc.replace("{faction}", nature)), false);
		itemToxic = ItemBuild.buildSkull(SkullUrl.BLACK_BERRY, 1, toxic, Arrays.asList(desc.replace("{faction}", toxic)), false);
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		itemBack = ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("back"), null, false);
	}

	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 45, this.name);
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, itemDeco);
			inv.setItem(i+36, itemDeco);
		}
		inv.setItem(19, itemDark);
		inv.setItem(21, itemLight);
		inv.setItem(23, itemNature);
		inv.setItem(25, itemToxic);
		inv.setItem(40, itemBack);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		PlayerGui playerG = getPlayerGui(player);
		int raw = e.getRawSlot();
		CardFaction faction;
		switch (raw) {
		
			case 40://back;
				playerG.getPreviousGui().open(player);
				return;
		
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
		DeckCards deck = playerG.getPlayerFeature().getDeck(playerG.getDeckEdit());
		for (int i = 0; i < deck.size(); i++)
			deck.setCard(i, null);
		deck.setFaction(faction);
		playerG.uploadPlayerFeature(manager.getSorci().getManagerPlayers());
		playerG.getPreviousGui().open(player);
	}
	
}
