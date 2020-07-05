package fr.tangv.sorcicubespell.fight;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerFight {

	private Player player;
	private FightDeck deck;
	private int mana;
	
	public PlayerFight(Player player, FightDeck deck) {
		this.player = player;
		this.deck = deck;
		this.setMana(0);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public FightDeck getDeck() {
		return deck;
	}
	
	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}
	
	//function

	public void showPlayer(PlayerFight player) {
		this.player.showPlayer(player.getPlayer());
	}
	
	public boolean isPlayer(Player player) {
		return this.player.equals(player);
	}
	
	public boolean teleport(Location loc) {
		return player.teleport(loc, TeleportCause.PLUGIN);
	}
	
}
