package fr.tangv.sorcicubespell.npc;

import java.util.List;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class MessageNPC implements ClickNPC {

	private final List<String> messages;
	
	public MessageNPC(List<String> messages) {
		this.messages = messages;
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		player.sendMessage(this.messages.get((int) (Math.random()*messages.size())).replace("{player}", player.getName()));
	}
	
}
