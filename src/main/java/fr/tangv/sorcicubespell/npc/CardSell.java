package fr.tangv.sorcicubespell.npc;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardSell extends PCSell {

	private Card card;
	
	public CardSell(SorciCubeSpell sorci, ConfigurationSection config, int price, String id) {
		super(sorci, config, price);
		try {
			this.card = sorci.getManagerCards().getCard(UUID.fromString(id));
		} catch (Exception e) {
			this.card = null;
		}
		if (this.isValid()) {
			this.itemView = CardRender.cardToItem(card, sorci);
			this.initItemSell("card", card.renderName());
		} else {
			this.itemView = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, config.getString("card_error"), Arrays.asList(id), false);
		}
	}
	
	@Override
	public boolean buy(PlayerGui player) {
		PlayerFeature feature = player.getPlayerFeature();
		String cardID = card.getUUID().toString();
		if (!feature.getCardsUnlocks().contains(cardID)) {
			feature.removeMoney(price);
			feature.getCardsUnlocks().add(cardID);
			player.uploadPlayerFeature(sorci.getManagerPlayers());
			player.getPlayer().sendMessage(getMessage("message_packet_buy_card")
					.replace("{name}", card.renderName())
					.replace("{price}", Integer.toString(price))
			);
			return true;
		} else {
			player.getPlayer().sendMessage(getMessage("message_packet_already_card")
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
