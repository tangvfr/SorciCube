package fr.tangv.sorcicubespell.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandRefresh implements CommandExecutor {

	private final SorciCubeSpell sorci;
	
	public CommandRefresh(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		try {
			sorci.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(sorci);
		}
		sender.sendMessage(sorci.config().messages.refresh.value);
		return true;
	}

}
