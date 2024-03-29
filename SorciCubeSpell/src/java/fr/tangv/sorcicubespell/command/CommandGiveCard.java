package fr.tangv.sorcicubespell.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.fight.FightSpectator;
import fr.tangv.sorcicubespell.fight.PlayerFight;
import fr.tangv.sorcicubespell.gui.PlayerGui;

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
					sorci.config().messages.playerNoFound.value
					.replace("{player}", args[0])
				);
			} else {
				Card card = sorci.getHandlerCards().get(UUID.fromString(args[1]));
				if (card == null) {
					sender.sendMessage(
						sorci.config().messages.invalidCard.value
						.replace("{uuid}", args[1])
					);
				} else {
					if (sorci.isLobby()) {
						PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
						PlayerFeatures feature = playerG.getPlayerFeatures();
						if (feature != null) {
							String uuid = card.getUUID().toString();
							if (!feature.getCardsUnlocks().contains(uuid))
								feature.getCardsUnlocks().add(uuid);
							playerG.uploadPlayerFeatures(sorci.getHandlerPlayers());
						}
					} else if (sorci.getManagerFight().isSpectator(player.getUniqueId())) {
						FightSpectator spectator = sorci.getManagerFight().getSpectator(player.getUniqueId());
						if (spectator.isFightPlayer()) {
							PlayerFight pf = (PlayerFight) spectator;
							pf.giveCard(card, 1);
							pf.initHotBar();
						}
					}
					sender.sendMessage(
						sorci.config().messages.giveCard.value
						.replace("{player}", player.getName())
						.replace("{card}", card.renderName())
					);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
