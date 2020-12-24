package fr.tangv.sorcicubespell.npc;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import fr.tangv.sorcicubespell.manager.ManagerClickNPC;

public class EventClickNPC implements Listener {

	private ManagerClickNPC manager;
	
	public EventClickNPC(ManagerClickNPC manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void clickNPC(PlayerInteractAtEntityEvent e) {
		if (e.getHand() == EquipmentSlot.HAND && e.getRightClicked() instanceof HumanEntity)
			manager.excuteClickNPC(e.getPlayer(), e.getRightClicked().getCustomName());
	}
	
}
