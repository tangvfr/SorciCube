package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiAdminViewCards extends AbstractGuiCards {
	
	public GuiAdminViewCards(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_admin_view_cards"));
	}

	@Override
	protected ItemStack createEndItem() {
		return ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("item_name.close"), null, false);
	}

	@Override
	protected void actionEndItem(PlayerGui player) {
		player.getPlayer().closeInventory();
	}

	@Override
	protected List<Card> listCards(PlayerGui player) {
		ArrayList<Card> cards = new ArrayList<Card>();
		for (Card card : manager.getSorci().getHandlerCards().cloneCardsValue()) {
			if (!player.isViewHideCards() && card.getFeatures().hasFeature(CardFeatureType.HIDE_CARD))
				continue;
			cards.add(card);
		}
		return cards;
	}
	
}
