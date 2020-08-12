package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.tangv.sorcicubespell.fight.FightType;
import fr.tangv.sorcicubespell.fight.PreFightData;
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
		PlayerFeature playerF = manager.getSorci().getManagerPlayers().getPlayerFeature(player);
		if (playerF.getUnlockDecks() >= number) {
			if (playerF.getDeck(number).isComplet()) {
				playerG.setDeckEdit(number);
				ManagerCreatorFight cf = sorci.getManagerCreatorFight();
				switch(playerG.getFightType()) {
					case UNCLASSIFIED:
						if (cf.getNoClassified() == null) {
							cf.setNoClassified(player);
							sorci.getManagerPreFightData().removePreFightData(player.getUniqueId());
							player.sendMessage(sorci.getMessage().getString("message_wait_fight"));
							break;
						} else {
							Player player1 = cf.getNoClassified();
							cf.setNoClassified(null);
							sorci.getManagerPreFightData().removePreFightData(player.getUniqueId());
							sorci.getManagerPreFightData().addPreFightData(
									new PreFightData(
											player1.getUniqueId(),
											player.getUniqueId(),
											getPlayerGui(player1).getDeckEdit(),
											playerG.getDeckEdit(),
											FightType.UNCLASSIFIED
										)
								);
							sorci.sendPlayerToServer(player, sorci.getNameServerFight());
							sorci.sendPlayerToServer(player1, sorci.getNameServerFight());
							return;
						}
						
					case DUEL:
						/*
							+ path rotate player
						
							- add ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE
						
							- add history and add view entity with stick
						
							- (path bug incitement) after test || give feature
						
							- add after and add anti afk
							- add system level and experience
						*/
						break;
				}
				Location loc = sorci.getManagerCreatorFight().getLocationFor(player);
				if (loc != null)
					player.teleport(loc);
			} else {
				player.sendMessage(sorci.getMessage().getString("message_invalid_deck"));
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 1F, 1F);
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
