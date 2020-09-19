package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.player.DeckCards;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiSwapCard extends AbstractGuiCards {
	
	public GuiSwapCard(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_swap_card"));
	}

	@Override
	protected ItemStack createEndItem() {
		return ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("item_name.back"), null, false);
	}

	@Override
	protected void actionEndItem(PlayerGui player) {
		manager.getSorci().getManagerGui().getGuiEditDeck().open(player.getPlayer());
	}

	@Override
	protected List<Card> listCards(PlayerGui player) {
		PlayerFeature playerF = player.getPlayerFeature();
		DeckCards deck = playerF.getDeck(player.getDeckEdit());
		ArrayList<Card> cards = new ArrayList<Card>();
		for (String uuidS : playerF.getCardsUnlocks()) {
			Card card = manager.getSorci().getManagerCards().getCard(UUID.fromString(uuidS));
			if (card != null) {
				if (card.getFaction() == CardFaction.BASIC || card.getFaction() == deck.getFaction()) {
					cards.add(card);
				}
			}
		}
		for (int i = 0; i < deck.size(); i++) {
			Card card = deck.getCard(i);
			if (card != null && cards.contains(card))
				cards.remove(card);
		}
		return cards;
	}

	@Override
	protected void actionOtherItem(PlayerGui player, ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			if (lore.size() >= 1) {
				String uuid = lore.get(lore.size()-1).replaceFirst("§8Id: ", "");
				Card card = manager.getSorci().getManagerCards().getCard(UUID.fromString(uuid));
				player.getPlayerFeature().getDeck(player.getDeckEdit()).setCard(player.getDeckCardEdit(), card);
				player.uploadPlayerFeature(manager.getSorci().getManagerPlayers());
				manager.getSorci().getManagerGui().getGuiEditDeck().open(player.getPlayer());
			}
		}
	}
	
}
