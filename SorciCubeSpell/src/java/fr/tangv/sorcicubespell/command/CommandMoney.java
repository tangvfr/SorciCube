package fr.tangv.sorcicubespell.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class CommandMoney implements CommandExecutor, TabCompleter {

	private SorciCubeSpell sorci;
	
	public CommandMoney(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length >= 2) {
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {
				PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
				if (playerG != null && playerG.getPlayerFeature() != null) {
					PlayerFeature feature = playerG.getPlayerFeature();
					String action = args[1];
					if (action.equalsIgnoreCase("get") || action.equalsIgnoreCase("set") 
							|| action.equalsIgnoreCase("add") || action.equalsIgnoreCase("remove")) {
						if (!action.equalsIgnoreCase("get")) {
							int value = -1;
							if (args.length >= 3)
								try {
									value = Integer.parseInt(args[2]);
								} catch (Exception e) {}
							if (value < 0) {
								sender.sendMessage(sorci.getMessage().getString("message_number_invalid"));
								return true;
							} else {
								String messageName;
								switch (action) {
								
									case "set":
										feature.setMoney(value);
										messageName = "message_money_set";
										break;
	
									case "add":
										feature.addMoney(value);
										messageName = "message_money_add";
										break;
										
									case "remove":
										feature.removeMoney(value);
										messageName = "message_money_remove";
										break;
										
									default:
										return false;
								}
								playerG.uploadPlayerFeature(sorci.getManagerPlayers());
								sender.sendMessage(sorci.getMessage().getString(messageName)
										.replace("{player}", player.getName())
										.replace("{money}", Integer.toString(value))
								);
							}
						}
						sender.sendMessage(sorci.getMessage().getString("message_money_get")
								.replace("{player}", player.getName())
								.replace("{money}", Integer.toString(feature.getMoney()))
						);
					} else {
						sender.sendMessage(sorci.getMessage().getString("message_action_invalid"));
					}
				} else {
					sender.sendMessage(sorci.getMessage().getString("message_player_no_feature").replace("{player}", player.getName()));
				}
			} else {
				sender.sendMessage(sorci.getMessage().getString("message_player_no_found").replace("{player}", args[0]));
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers())
				list.add(player.getName());
		} else if (args.length == 2) {
			list.add("get");
			list.add("set");
			list.add("add");
			list.add("remove");
		}
		return list;
	}

}
