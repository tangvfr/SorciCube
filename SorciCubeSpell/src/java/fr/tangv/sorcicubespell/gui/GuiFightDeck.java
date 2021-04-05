package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.tangv.sorcicubecore.fight.FightType;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;

public class GuiFightDeck extends GuiDecks {

	public GuiFightDeck(ManagerGui manager) {
		super(manager);
	}

	private void chooseDeck(Player player, int number) throws Exception {
		PlayerGui playerG = getPlayerGui(player);
		if (playerG.getPlayerFeatures().getUnlockDecks() >= number) {
			if (playerG.getPlayerFeatures().getDeck(number).isComplet()) {
				playerG.setDeckEdit(number);
				ManagerCreatorFight cf = sorci.getManagerCreatorFight();
				sorci.getHandlerFightData().removeFightDataPlayer(player.getUniqueId());
				switch(playerG.getFightType()) {
					case UNCLASSIFIED:
						if (cf.getNoClassified() == null) {
							cf.setNoClassified(player);
							player.sendMessage(sorci.getMessage().getString("message_wait_fight"));
							break;
						} else {
							Player player1 = cf.getNoClassified();
							cf.setNoClassified(null);
							manager.getSorci().getManagerCreatorFight().startFightPlayer(
								getPlayerGui(player1),
								playerG,
								manager.getSorci().getNameServerFight(),
								FightType.UNCLASSIFIED
							);
							return;
						}
						
					case DUEL:
						playerG.setInviteDuel(null);
						cf.addInDuel(player);
						player.sendMessage(sorci.getMessage().getString("message_wait_duel"));
						break;
						
					default:
						break;
				}
				Location loc = sorci.getManagerCreatorFight().getLocationFor(player);
				if (loc != null)
					player.teleport(loc);
			} else {
				player.sendMessage(sorci.getMessage().getString("message_invalid_deck"));
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 1.0F, 1.5F);
			}
		}
	}
	
	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		try {
			int raw = e.getRawSlot();
			switch (raw) {
				case 18://deck 1
					chooseDeck(player, 1);
					break;
		
				case 20://deck 2
					chooseDeck(player, 2);
					break;
					
				case 22://deck 3
					chooseDeck(player, 3);
					break;
					
				case 24://deck 4
					chooseDeck(player, 4);
					break;
					
				case 26://deck 5
					chooseDeck(player, 5);
					break;
					
				case 40://back
					manager.getGuiFight().open(player);
					break;
					
				default:
					break;
			}
		} catch (Exception e1) {
			Bukkit.getLogger().throwing("GuiFightDeck" ,"onClick", e1);
		}
	}

}
