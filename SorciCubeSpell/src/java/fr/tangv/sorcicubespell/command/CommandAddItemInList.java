package fr.tangv.sorcicubespell.command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandAddItemInList implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandAddItemInList(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(sorci.getMessage().getString("message_no_player"));
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 1) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null) {
				String name = args[0];
				if (!name.isEmpty() && !name.contains(".")) {
					if (sorci.getConfigItemList().get(name) == null) {
						sorci.getConfigItemList().set(name, item);
						try {
							sorci.getConfigItemList().save();
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(sorci.getMessage().getString("message_itemlist_saved").replace("{name}", name));
					} else
						player.sendMessage(sorci.getMessage().getString("message_itemlist_already_name"));
				} else {
					player.sendMessage(sorci.getMessage().getString("message_itemlist_invalid_name"));
				}
			} else {
				player.sendMessage(sorci.getMessage().getString("message_itemlist_no_item"));
			}
			return true;
		}
		return false;
	}

}
