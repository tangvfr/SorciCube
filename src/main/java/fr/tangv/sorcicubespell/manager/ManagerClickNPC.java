package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.npc.ClickNPC;
import fr.tangv.sorcicubespell.npc.EventClickNPC;

public class ManagerClickNPC {

	private SorciCubeSpell sorci;
	private HashMap<String, ClickNPC> clickNPCs;
	
	public ManagerClickNPC(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.clickNPCs = new HashMap<String, ClickNPC>();
		//init click npc
		//clickNPCs.put(sorci.getParameter().getString("con"), new ClickNPCCON());
		//init bukkit
		Bukkit.getPluginManager().registerEvents(new EventClickNPC(this), sorci);
	}
	
	public void excuteClickNPC(Player player, String nameNPC) {
		Bukkit.broadcastMessage("nameNPC: "+nameNPC);
		if (clickNPCs.containsKey(nameNPC))
			clickNPCs.get(nameNPC).clickNPC(sorci, nameNPC, player);
	}
	
}
