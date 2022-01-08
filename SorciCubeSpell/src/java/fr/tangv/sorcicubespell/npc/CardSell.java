package fr.tangv.sorcicubespell.npc;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.configs.GuiSellerPacketsGuiConfig;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardSell extends PCSell {

	private Card card;
	
	public CardSell(SorciCubeSpell sorci, int price, String id) {
		super(sorci, price);
		try {
			this.card = sorci.getHandlerCards().get(UUID.fromString(id));
		} catch (Exception e) {
			this.card = null;
		}
		GuiSellerPacketsGuiConfig gui = sorci.config().gui.guiSellerPackets;
		if (isValid()) {
			this.itemView = CardRender.cardToItem(card, sorci);
			this.initItemSell(gui, false, card.renderName());
		} else {
			this.itemView = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, gui.cardError.value, Arrays.asList(id), false);
		}
	}
	
	@Override
	public boolean buy(PlayerGui player) {
		PlayerFeatures feature = player.getPlayerFeatures();
		String cardID = card.getUUID().toString();
		if (!feature.getCardsUnlocks().contains(cardID)) {
			feature.removeMoney(price);
			feature.getCardsUnlocks().add(cardID);
			player.uploadPlayerFeatures(sorci.getHandlerPlayers());
			player.getPlayer().sendMessage(sorci.config().messages.packetBuyCard.value
					.replace("{name}", card.renderName())
					.replace("{price}", Integer.toString(price))
			);
			return true;
		} else {
			player.getPlayer().sendMessage(sorci.config().messages.packetAlreadyCard.value
					.replace("{name}", card.renderName())
			);
			player.getPlayer().closeInventory();
			return false;
		}
	}

	@Override
	public boolean isValid() {
		return card != null;
	}
	
}
