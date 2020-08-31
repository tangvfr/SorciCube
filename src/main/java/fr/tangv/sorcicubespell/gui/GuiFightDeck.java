package fr.tangv.sorcicubespell.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.fight.FightType;
import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.RenderException;

public class GuiFightDeck extends GuiDecks {

	public GuiFightDeck(ManagerGui manager) {
		super(manager);
	}

	private void chooseDeck(Player player, int number) throws Exception {
		PlayerGui playerG = getPlayerGui(player);
		PlayerFeature playerF = manager.getSorci().getManagerPlayers().getPlayerFeature(player.getUniqueId());
		if (playerF.getUnlockDecks() >= number) {
			if (playerF.getDeck(number).isComplet()) {
				playerG.setDeckEdit(number);
				ManagerCreatorFight cf = sorci.getManagerCreatorFight();
				sorci.getManagerPreFightData().removeFightDataPlayer(player.getUniqueId());
				switch(playerG.getFightType()) {
					case UNCLASSIFIED:
						if (cf.getNoClassified() == null) {
							cf.setNoClassified(player);
							player.sendMessage(sorci.getMessage().getString("message_wait_fight"));
							break;
						} else {
							Player player1 = cf.getNoClassified();
							cf.setNoClassified(null);
							String server = sorci.getNameServerFight();
							sorci.getManagerPreFightData().addFightData(
									new FightData(
											UUID.randomUUID(),
											player1.getUniqueId(),
											player.getUniqueId(),
											getPlayerGui(player1).getDeckEdit(),
											playerG.getDeckEdit(),
											FightType.UNCLASSIFIED,
											FightStat.WAITING,
											server
										)
								);
							sorci.sendPlayerToServer(player, server);
							sorci.sendPlayerToServer(player1, server);
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
			Bukkit.getLogger().warning(RenderException.renderException(e1));
		}
	}

}
