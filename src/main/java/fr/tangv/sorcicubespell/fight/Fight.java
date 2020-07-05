package fr.tangv.sorcicubespell.fight;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class Fight {

	private SorciCubeSpell sorci;
	private PlayerFight player1;
	private PlayerFight player2;
	private FightArena arena;
	//end
	private boolean isEnd;
	private Player losser;
	
	public Fight(SorciCubeSpell sorci, PreFight preFight, Player player2Arg) throws Exception {
		this.sorci = sorci;
		//player1 start one
		if (Math.random() < 0.5) {
			this.player1 = new PlayerFight(preFight.getPlayer1(),
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(preFight.getPlayer1()).getDeck(preFight.getPlayer1DeckUse())));
			this.player2 = new PlayerFight(player2Arg,
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player2Arg).getDeck(preFight.getPlayer2DeckUse())));
		} else {
			this.player2 = new PlayerFight(preFight.getPlayer1(),
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(preFight.getPlayer1()).getDeck(preFight.getPlayer1DeckUse())));
			this.player1 = new PlayerFight(player2Arg,
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player2Arg).getDeck(preFight.getPlayer2DeckUse())));
		}
		this.isEnd = false;
		this.losser = null;
		//init fight
		this.arena = sorci.getManagerFight().pickArena();
		//init player
		player1.teleport(arena.getFirstBase());
		player2.teleport(arena.getSecondBase());
		player1.showPlayer(player2);
		player2.showPlayer(player1);
		
	}
	
	public void update() {
		
	}
	
	//for end
	public void setEnd(Player losser) {
		this.losser = losser;
		this.isEnd = true;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	
	public void end() {
		end(losser);
	}
	
	//player is player loss
	public void end(Player losser) {
		end(losser, player1.isPlayer(losser) ? player2.getPlayer() : player1.getPlayer());
	}
	
	private void end(Player losser, Player winner) {
		if (losser.isOnline()) {
			
		}
		if (winner.isOnline()) {
			
		}
	}
	
	//geting seting
	
	public PlayerFight getPlayer1() {
		return player1;
	}

	public PlayerFight getPlayer2() {
		return player2;
	}
	
}
