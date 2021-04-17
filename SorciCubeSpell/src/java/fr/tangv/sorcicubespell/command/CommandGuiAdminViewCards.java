package fr.tangv.sorcicubespell.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.manager.ManagerGui;

public class CommandGuiAdminViewCards implements CommandExecutor {

	private ManagerGui manager;
	
	public CommandGuiAdminViewCards(ManagerGui manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			this.manager.getPlayerGui(player).setViewHideCards((args.length >= 1) ? args[0].equalsIgnoreCase("true") : false);
			this.manager.getGuiAdminViewCards().open(player);
			return true;
		} else {
			sender.sendMessage(manager.getSorci().config().messages.playerNoFound.value);
			return true;
		}
	}
	
}
