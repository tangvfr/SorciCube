package fr.tangv.sorcicubespell.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.player.PlayerFeatures;
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
				if (playerG != null && playerG.getPlayerFeatures() != null) {
					PlayerFeatures feature = playerG.getPlayerFeatures();
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
								sender.sendMessage(sorci.config().messages.numberInvalid.value);
								return true;
							} else {
								String message;
								switch (action) {
								
									case "set":
										feature.setMoney(value);
										message = sorci.config().messages.moneySet.value;
										break;
	
									case "add":
										feature.addMoney(value);
										message = sorci.config().messages.moneyAdd.value;
										break;
										
									case "remove":
										feature.removeMoney(value);
										message = sorci.config().messages.moneyRemove.value;
										break;
										
									default:
										return false;
								}
								playerG.uploadPlayerFeatures(sorci.getHandlerPlayers());
								sender.sendMessage(message
										.replace("{player}", player.getName())
										.replace("{money}", Integer.toString(value))
								);
							}
						}
						sender.sendMessage(sorci.config().messages.moneyGet.value
								.replace("{player}", player.getName())
								.replace("{money}", Integer.toString(feature.getMoney()))
						);
					} else {
						sender.sendMessage(sorci.config().messages.actionInvalid.value);
					}
				} else {
					sender.sendMessage(sorci.config().messages.playerNoFeature.value.replace("{player}", player.getName()));
				}
			} else {
				sender.sendMessage(sorci.config().messages.playerNoFound.value.replace("{player}", args[0]));
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
