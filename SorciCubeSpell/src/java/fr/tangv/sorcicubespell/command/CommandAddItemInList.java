package fr.tangv.sorcicubespell.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.ItemConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandAddItemInList implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandAddItemInList(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(sorci.config().messages.noPlayer.value);
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 1) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item != null) {
				String name = args[0];
				if (!name.isEmpty() && !name.contains(".")) {
					for (ItemConfig i : sorci.config().items)
						if (i.itemName.value.equals(name)) {
							player.sendMessage(sorci.config().messages.itemlistAlreadyName.value);
							return true;
						}
					YamlConfiguration config = new YamlConfiguration();
					config.set("item", item);
					try {
						sorci.config().items.add(new ItemConfig(name, config.saveToString()));
						sorci.uploadConfig();
					} catch (Exception e) {
						e.printStackTrace();
						player.sendMessage(e.getMessage());
					}
					player.sendMessage(sorci.config().messages.itemlistSaved.value.replace("{name}", name));
				} else {
					player.sendMessage(sorci.config().messages.itemlistInvalidName.value);
				}
			} else {
				player.sendMessage(sorci.config().messages.itemlistNoItem.value);
			}
			return true;
		}
		return false;
	}

}
