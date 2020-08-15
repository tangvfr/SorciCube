package fr.tangv.sorcicubespell.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.player.PlayerFeature;

public class CommandGiveCard implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandGiveCard(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length < 2) return false;
		try {
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(
					sorci.getMessage().getString("message_player_no_found")
					.replace("{player}", args[0])
				);
			} else {
				Card card = sorci.getManagerCards().getCard(UUID.fromString(args[1]));
				if (card == null) {
					sender.sendMessage(
						sorci.getMessage().getString("message_invalid_card")
						.replace("{uuid}", args[1])
					);
				} else {
					if (sorci.isLobby() || !sorci.getManagerFight().getPlayerFights().containsKey(player)) {
						PlayerFeature feature = sorci.getManagerPlayers().getPlayerFeature(player);
						if (feature != null) {
							String uuid = card.getUUID().toString();
							if (!feature.getCardsUnlocks().contains(uuid))
								feature.getCardsUnlocks().add(uuid);
							sorci.getManagerPlayers().update(feature);
						}
						player.getInventory().addItem(CardRender.cardToItem(card, sorci));
					} else {
						PlayerFight pf = sorci.getManagerFight().getPlayerFights().get(player);
						pf.giveCard(card, 1);
						pf.initHotBar();
					}
					sender.sendMessage(
						sorci.getMessage().getString("message_give_card")
						.replace("{player}", player.getName())
						.replace("{card}", card.getName())
					);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
