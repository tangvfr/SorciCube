package fr.tangv.sorcicubespell.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiEditDeckGuiConfig;
import fr.tangv.sorcicubecore.player.DeckCards;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiEditDeck extends AbstractGui<GuiEditDeckGuiConfig> {

	private ItemStack itemDeco;
	private ItemStack itemBack;
	
	public GuiEditDeck(ManagerGui manager) {
		super(manager, manager.getSorci().config().gui.guiEditDeck);
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		itemBack = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 14, config.back.value, null, false);
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 36, config.name.value);
		PlayerGui playerG = getPlayerGui(player);
		DeckCards deck = playerG.getPlayerFeatures().getDeck(playerG.getDeckEdit());
		String averageCost = "§b"+Double.toString(deck.calcAverageCost()/10.0D)+" \u2756";
		ItemStack itemTypeDeck = ItemBuild.buildSkull(
				SkullUrl.getSkullForFaction(deck.getFaction()),
				1,
				manager.getSorci().getEnumTool().factionToString(deck.getFaction()),
				Arrays.asList(config.loreFaction.toArrayString()),
				false
			);
		ItemStack itemDeck = ItemBuild.buildItem(
				Material.BOOK,
				1,
				(short) 0,
				(byte) 0,
				config.deck.value
					.replace("{number}", Integer.toString(playerG.getDeckEdit()))
					.replace("{average_cost}", averageCost)
				,
				null,
				false);
		//set items
		inv.setItem(0, itemDeco); inv.setItem(1, itemDeco); inv.setItem(2, itemDeco); inv.setItem(8, itemBack);
		inv.setItem(9, itemDeco); inv.setItem(10, itemTypeDeck); inv.setItem(11, itemDeco); inv.setItem(17, itemBack);
		inv.setItem(18, itemDeco); inv.setItem(19, itemDeck); inv.setItem(20, itemDeco); inv.setItem(26, itemBack);
		inv.setItem(27, itemDeco); inv.setItem(28, itemDeco); inv.setItem(29, itemDeco); inv.setItem(35, itemBack);
		for (int i = 0; i < deck.size(); i++)
			inv.setItem(((i/5)*9)+3+(i%5), CardRender.cardToItem(deck.getCard(i),manager.getSorci()));
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		if (raw == 8 || raw == 17 || raw == 26 || raw == 35) {//back
			manager.getSorci().getManagerGui().getGuiDecks().open(player);
		} else if (raw == 10) {
			getPlayerGui(player).setPreviousGui(this);
			manager.getGuiCreateDeck().open(player);
		} else {
			int a = (raw%9)-3;
			if (a >= 0 && a < 5) {
				getPlayerGui(player).setDeckCardEdit(((raw/9)*5)+a);
				manager.getSorci().getManagerGui().getGuiSwapCard().open(player);
			}
		}
		e.setCancelled(true);
	}

}
