package fr.tangv.sorcicubespell.gui;

import org.bukkit.Bukkit;
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
							break;
						} else {
							Player player1 = cf.getNoClassified();
							cf.setNoClassified(null);
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
						//add after
						break;
				}
				player.teleport(sorci.getManagerCreatorFight().getLocationFor(player));
			}
		}
	}
	
	@Override
	public void onClick(Player player, InventoryClickEvent e) {
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
		e.setCancelled(true);
	}

}