package fr.tangv.sorcicubespell.player;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.Config;

public class RunnableActionBar implements Runnable {

	private Config lc;
	private String messageActionBar;
	private ConcurrentHashMap<Player, PlayerGui> playerGuis;
	
	public RunnableActionBar(SorciCubeSpell sorci, ConcurrentHashMap<Player, PlayerGui> playerGuis) {
		this.lc = sorci.getLevelConfig();
		this.messageActionBar = sorci.getParameter().getString("action_bar_message");
		this.playerGuis = playerGuis;
	}
	
	@Override
	public void run() {
		for (PlayerGui playerG : playerGuis.values())
			playerG.updateDisplay(lc, messageActionBar);
	}
	
	public void updateOnePlayer(PlayerGui playerG) {
		playerG.updateDisplay(lc, messageActionBar);
	}
	
}
