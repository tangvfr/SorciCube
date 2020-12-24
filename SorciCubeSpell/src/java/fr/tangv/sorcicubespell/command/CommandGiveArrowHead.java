package fr.tangv.sorcicubespell.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemHead;

public class CommandGiveArrowHead implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandGiveArrowHead(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			PlayerInventory inv = ((Player) sender).getInventory();
			inv.addItem(ItemHead.SELECTABLE_SPELL);
			inv.addItem(ItemHead.SELECTABLE_ENTITY_ATTACK);
			inv.addItem(ItemHead.SELECTABLE_ENTITY_DAMAGE);
			inv.addItem(ItemHead.SELECTABLE_ALL_SPELL);
			inv.addItem(ItemHead.SELECTABLE_ENTITY_AAE);
			inv.addItem(ItemHead.SELECTABLE_POSE);
			inv.addItem(ItemHead.SELECTED_ENTITY);
			sender.sendMessage(sorci.getMessage().getString("message_give_head"));
		} else {
			sender.sendMessage(sorci.getMessage().getString("message_no_player"));
		}
		return true;
	}

}
