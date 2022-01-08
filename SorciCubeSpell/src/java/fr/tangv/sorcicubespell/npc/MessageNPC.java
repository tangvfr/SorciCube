package fr.tangv.sorcicubespell.npc;

import java.util.List;
import java.util.Vector;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.config.StringConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class MessageNPC implements ClickNPC {

	private final Vector<String> messages;
	
	public MessageNPC(List<StringConfig> messages) {
		this.messages = new Vector<String>();
		for (StringConfig msg : messages)
			this.messages.add(msg.value);
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (messages.size() > 0)
			player.sendMessage(this.messages.get((int) (Math.random()*messages.size())).replace("{player}", player.getName()));
	}
	
}
