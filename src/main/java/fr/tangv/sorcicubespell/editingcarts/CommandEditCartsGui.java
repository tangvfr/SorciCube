package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEditCartsGui implements CommandExecutor {

	private EditCartsGui ec;
	
	public CommandEditCartsGui(EditCartsGui ec) {
		this.ec = ec;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			this.ec.guiEditList.open(player);
			return true;
		} else {
			sender.sendMessage(ec.sorci.getMessage().getString("message_no_player"));
		}
		return false;
	}
	
}
