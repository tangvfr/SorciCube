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
	
	public Fight(SorciCubeSpell sorci, PreFight preFight, Player player2) throws Exception {
		this.sorci = sorci;
		this.player1 = preFight.getPlayer1();
		this.player2 = player2;
		this.deck1 = new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player1).getDeck(preFight.getPlayer1DeckUse()));
		this.deck1 = new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player2).getDeck(preFight.getPlayer2DeckUse()));
		this.isEnd = false;
		//init fight
		
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
