package fr.tangv.sorcicubespell.fight;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerFight {

	private Fight fight;
    private PlayerFight enemie;
	private Player player;
	private FightDeck deck;
	private boolean first;
	private int mana;
	private FightEntity[] entity;
	private Location locBase;
	
	public PlayerFight(Fight fight, Player player, FightDeck deck, boolean first) {
		this.fight = fight;
		this.player = player;
		this.deck = deck;
		this.setMana(0);
		this.first = first;
		this.locBase = this.isFisrt() ? fight.getArena().getFirstBase() : fight.getArena().getSecondBase();
		//entity
		Location[] locs = this.isFisrt() ? fight.getArena().getFirstEntity() : fight.getArena().getSecondEntity();
		this.entity = new FightEntity[locs.length];
		for (int i = 0; i < locs.length; i++)
			this.entity[i] = new FightEntity(fight, locs[i]);
	}
	
	public Fight getFight() {
		return fight;
	}
	
	public boolean isFisrt() {
		return first;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public FightDeck getDeck() {
		return deck;
	}
	
	public FightEntity getEntity(int index) {
		return entity[index];
	}
	
	public int getMaxEntity() {
		return entity.length;
	}
	
	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public PlayerFight getEnemie() {
		return enemie;
	}

	public void setEnemie(PlayerFight enemie) {
		this.enemie = enemie;
		player.showPlayer(enemie.getPlayer());
	}
	
	//function
	
	public void initHotBar() {
		boolean play = canPlay();
		//set bar card main etc
		//player.getInventory().set
	}
	
	public boolean canPlay() {
		return fight.gameIsStart() && fight.getFirstPlay() == first;
	}
	
	public boolean isPlayer(Player player) {
		return this.player.equals(player);
	}
	
	public boolean teleportToBase() {
		return player.teleport(this.locBase, TeleportCause.PLUGIN);
	}
	
}
