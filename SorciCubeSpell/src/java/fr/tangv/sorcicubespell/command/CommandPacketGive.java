package fr.tangv.sorcicubespell.command;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.packet.PacketCards;
import fr.tangv.sorcicubespell.manager.ManagerPakcetCards;

public class CommandPacketGive implements CommandExecutor, TabCompleter {
	
	private ManagerPakcetCards manager;
	
	public CommandPacketGive(ManagerPakcetCards manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length < 2) return false;
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(
				manager.getSorci().getMessage().getString("message_player_no_found")
				.replace("{player}", args[0])
			);
			return true;
		}
		String name = args[1];
		for (int i = 2; i < args.length; i++)
			name += " "+args[i];
		name = name.replace("&", "§");
		PacketCards packetCards = manager.getPacketCards(name);
		if (packetCards == null) {
			sender.sendMessage(
				manager.getSorci().getMessage().getString("message_packet_no_found")
				.replace("{name}", name)
			);
		} else {
			player.getInventory().addItem(manager.packetToItem(packetCards));
			sender.sendMessage(
				manager.getSorci().getMessage().getString("message_give_packet")
				.replace("{name}", name)
				.replace("{player}", player.getName())
			);
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers())
				list.add(player.getName());
		} else if (args.length >= 2) {
			Enumeration<String> names = manager.getEnumNamePacket();
			//test the name of packet
			while (names.hasMoreElements()) {
				String[] nameFind = names.nextElement().replace("§", "&").split(" ");
				boolean add = nameFind.length >= args.length-1;
				if (add) {
					for (int i = 0; i < args.length-2; i++) {
						if (!args[i+1].equals(nameFind[i]))
							add = false;
					}
					String textAdd = nameFind[args.length-2];
					if (add && !list.contains(textAdd)) 
						list.add(textAdd);
				}
			}
		}
		return list;
	}
		
}
