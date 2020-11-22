package fr.tangv.sorcicubespell.npc;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class MessageNPC implements ClickNPC {

	private final String message;
	
	public MessageNPC(String message) {
		this.message = message;
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		player.sendMessage(this.message.replace("{player}", player.getName()));
	}
	
}
