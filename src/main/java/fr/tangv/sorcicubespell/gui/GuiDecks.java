package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.player.DeckCards;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiDecks extends AbstractGui {

	private ItemStack itemDeco;
	private ItemStack itemBack;
	
	public GuiDecks(ManagerGui manager) {
		super(manager, manager.getSorci().gertGuiConfig().getConfigurationSection("gui_decks"));
		itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		itemBack = ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("back"), null, false);
	}

	private ItemStack getItemDeck(PlayerFeature playerF, int number) {
		String url = "";
		if (playerF.getUnlockDecks() >= number) {
			DeckCards deckCards = playerF.getDeck(number);
			url = SkullUrl.getSkullForFaction(deckCards.getFaction());
		} else {
			url = SkullUrl.CHEST_GRAY;
		}
		return ItemBuild.buildSkull(url, 1, config.getString("deck").replace("{number}", Integer.toString(number)), null, false);
	}
	
	private void editDeck(Player player, int number) throws Exception {
		PlayerGui playerG = getPlayerGui(player);
		PlayerFeature playerF = manager.getSorci().getManagerPlayers().getPlayerFeature(player);
		if (playerF.getUnlockDecks() >= number) {
			playerG.setDeckEdit(number);
			playerG.setPlayerFeature(playerF);
			if (playerF.getDeck(number).getFaction() != CardFaction.BASIC) {
				manager.getGuiEditDeck().open(player);
			} else {
				playerG.setPreviousGui(this);
				manager.getGuiCreateDeck().open(player);
			}
		}
	}
	
	@Override
	public Inventory getInventory(Player player) {
		try {
			PlayerFeature playerF = manager.getSorci().getManagerPlayers().getPlayerFeature(player);
			Inventory inv = Bukkit.createInventory(null, 45, this.name);
			for (int i = 0; i < 9; i++) {
				inv.setItem(i, itemDeco);
				inv.setItem(i+36, itemDeco);
			}
			inv.setItem(18, getItemDeck(playerF, 1));
			inv.setItem(20, getItemDeck(playerF, 2));
			inv.setItem(22, getItemDeck(playerF, 3));
			inv.setItem(24, getItemDeck(playerF, 4));
			inv.setItem(26, getItemDeck(playerF, 5));
			inv.setItem(40, itemBack);
			return inv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		try {
			int raw = e.getRawSlot();
			switch (raw) {
				case 18://deck 1
					editDeck(player, 1);
					break;
		
				case 20://deck 2
					editDeck(player, 2);
					break;
					
				case 22://deck 3
					editDeck(player, 3);
					break;
					
				case 24://deck 4
					editDeck(player, 4);
					break;
					
				case 26://deck 5
					editDeck(player, 5);
					break;
					
				case 40://back
					manager.getSorci().getManagerGui().getGuiEditOrView().open(player);
					break;
					
				default:
					break;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		e.setCancelled(true);
	}
	
}