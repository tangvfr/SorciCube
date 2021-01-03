package fr.tangv.sorcicubespell.command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandRefresh implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandRefresh(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		try {
			sorci.getHandlerCards().refresh();
			sender.sendMessage(sorci.getMessage().getString("message_refresh"));
		} catch (IOException | ReponseRequestException | RequestException e) {
			e.printStackTrace();
			sender.sendMessage("§8[§4ERROR§8] §c"+e.getMessage());
		}
		return true;
	}

}
