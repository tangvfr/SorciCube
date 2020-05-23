package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEditCartsGui implements CommandExecutor {

	private EditCartsGui gui;
	
	public CommandEditCartsGui(EditCartsGui gui) {
		this.gui = gui;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			
		} else {
			sender.sendMessage(gui.sorci.getMessage().getString("message_no_player"));
		}
		return false;
	}
	
}
