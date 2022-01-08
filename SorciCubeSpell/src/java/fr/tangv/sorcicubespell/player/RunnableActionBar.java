package fr.tangv.sorcicubespell.player;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class RunnableActionBar implements Runnable {

	private final ConcurrentHashMap<Player, PlayerGui> playerGuis;
	private final SorciCubeSpell sorci;
	
	public RunnableActionBar(SorciCubeSpell sorci, ConcurrentHashMap<Player, PlayerGui> playerGuis) {
		this.sorci = sorci;
		this.playerGuis = playerGuis;
	}
	
	@Override
	public void run() {
		for (PlayerGui playerG : playerGuis.values()) {
			if (playerG.getPlayerFeatures() != null)
				playerG.updateDisplay(sorci.config().parameter.actionBarMessage.value, sorci.config().level);
		}
	}
	
	public void updateOnePlayer(PlayerGui playerG) {
		playerG.updateDisplay(sorci.config().parameter.actionBarMessage.value, sorci.config().level);
	}
	
}
