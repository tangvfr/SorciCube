package fr.tangv.sorcicubespell.packet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.tangv.sorcicubespell.manager.ManagerPacketCards;

public class CommandPacketAdd implements CommandExecutor {

	private ManagerPacketCards manager;
	
	public CommandPacketAdd(ManagerPacketCards manager) {
		this.manager = manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length < 1) return false;
		String name = args[0];
		for (int i = 1; i < args.length; i++)
			name += " "+args[i];
		name = name.replace("&", "§");
		if (manager.containtPacket(name))
			sender.sendMessage(
				manager.getSorci().getMessage().getString("message_already_exist_packet_cards")
				.replace("{name}", name)
			);
		else {
			manager.newPacket(name);
			sender.sendMessage(
				manager.getSorci().getMessage().getString("message_create_packet_cards")
				.replace("{name}", name)
			);
		}
		return true;
	}
	
}
