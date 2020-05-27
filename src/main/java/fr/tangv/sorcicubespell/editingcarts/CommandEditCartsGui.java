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
			if (args.length > 0) {
				PlayerEditCart p = this.ec.editingCarts.get(player);
				if (p.getCart() != null && this.ec.guiBooks.containsKey(args[0]))
					return this.ec.guiBooks.get(args[0]).onCommand(p, args);
				return false;
			} else {
				this.ec.guiEditList.open(player);
				return true;
			}
		} else {
			sender.sendMessage(ec.sorci.getMessage().getString("message_no_player"));
		}
		return false;
	}
	
}
