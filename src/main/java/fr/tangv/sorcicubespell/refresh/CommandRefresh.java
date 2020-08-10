package fr.tangv.sorcicubespell.refresh;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandRefresh implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandRefresh(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		sorci.getManagerCards().refresh();
		sender.sendMessage(sorci.getMessage().getString("message_refresh"));
		return true;
	}

}
