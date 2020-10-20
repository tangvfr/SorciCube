package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.RenderException;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiViewCards extends AbstractGuiCards {

	public GuiViewCards(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_view_cards"));
	}

	@Override
	protected ItemStack createEndItem() {
		return ItemBuild.buildSkull(SkullUrl.BACK_RED, 1, config.getString("item_name.back"), null, false);
	}

	@Override
	protected void actionEndItem(PlayerGui player) {
		manager.getSorci().getManagerGui().getGuiEditOrView().open(player.getPlayer());
	}

	@Override
	protected List<Card> listCards(PlayerGui player) {
		try {
			ArrayList<Card> cards = new ArrayList<Card>();
			for (String uuidS : player.getPlayerFeature().getCardsUnlocks()) {
				Card card = manager.getSorci().getManagerCards().getCard(UUID.fromString(uuidS));
				if (card != null)
					cards.add(card);
			}
			return cards;
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
			return new ArrayList<Card>();
		}
	}
	
}