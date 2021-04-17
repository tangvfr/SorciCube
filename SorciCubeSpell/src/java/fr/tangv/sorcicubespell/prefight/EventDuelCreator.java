package fr.tangv.sorcicubespell.prefight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;

public class EventDuelCreator implements Listener {

	private final SorciCubeSpell sorci;
	private final ManagerCreatorFight manager;
	
	public EventDuelCreator(SorciCubeSpell sorci, ManagerCreatorFight manager) {
		this.sorci = sorci;
		this.manager = manager;
	}
	
	@EventHandler
	public void onClickOnPLayer(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Player && e.getHand() == EquipmentSlot.HAND) {
			Player player = e.getPlayer();
			Player cible = (Player) e.getRightClicked();
			if (manager.isInDuel(player) && manager.isInDuel(cible)) {
				e.setCancelled(true);
				PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
				PlayerGui cibleG = sorci.getManagerGui().getPlayerGui(cible);
				if (cibleG.getInviteDuel() == player) {
					manager.duelFightPlayer(playerG, cibleG, sorci.getNameServerFight());
				} else if (playerG.getInviteDuel() != cible) {
					playerG.setInviteDuel(cible);
					player.sendMessage(sorci.config().messages.messageDuelSendInvite.value.replace("{player}", cible.getName()));
					cible.sendMessage(sorci.config().messages.messageDuelReceiveInvite.value.replace("{player}", player.getName()));
				} else {
					player.sendMessage(sorci.config().messages.messageDuelAlreadyInvite.value.replace("{player}", cible.getName()));
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player player = e.getPlayer();
			if (manager.isInDuel(player)) {
				e.setCancelled(true);
				PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
				if (playerG.getInviteDuel() != null) {
					playerG.setInviteDuel(null);
					player.sendMessage(sorci.config().messages.messageDuelCancelInvite.value);
				}
			}
		}
	}
	
}
