package fr.tangv.sorcicubespell.prefight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class EventDuelCreator implements Listener {

	private SorciCubeSpell sorci;
	
	public EventDuelCreator(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	@EventHandler
	public void onClickOnPLayer(PlayerInteractAtEntityEvent e) {
		
	}
	
	@EventHandler
	public void onClickOnPLayer(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			/*message_duel_send_invite
			message_duel_receive_invite
			message_duel_cancel_invite*/
		}
	}
	
}
