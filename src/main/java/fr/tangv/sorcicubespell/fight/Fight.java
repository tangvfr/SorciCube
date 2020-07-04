package fr.tangv.sorcicubespell.fight;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class Fight {

	private SorciCubeSpell sorci;
	private Player player1;
	private Player player2;
	private FightDeck deck1;
	private FightDeck deck2;
	private boolean isEnd;
	
	public Fight(SorciCubeSpell sorci, PreFight preFight, Player player2) {
		this.sorci = sorci;
		this.player1 = preFight.getPlayer1();
		this.player2 = player2;
		/*deck1
		deck2*/
		this.isEnd = false;
	}
	
	public void update() {
		
	}
	
	//for end
	
	public boolean isEnd() {
		return isEnd;
	}
	
	public void end() {
		
	}
	
	//player is player loss
	public void end(Player player) {
		
	}
	
	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}
