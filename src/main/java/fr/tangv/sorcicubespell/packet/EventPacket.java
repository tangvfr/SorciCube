package fr.tangv.sorcicubespell.packet;

import org.bukkit.event.Listener;

import fr.tangv.sorcicubespell.manager.ManagerPacketCards;

public class EventPacket implements Listener {

	private ManagerPacketCards manager;
	
	public EventPacket(ManagerPacketCards manager) {
		this.manager = manager;
	}
	
	
	
}
