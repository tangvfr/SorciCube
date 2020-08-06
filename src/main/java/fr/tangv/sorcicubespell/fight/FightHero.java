package fr.tangv.sorcicubespell.fight;

import fr.tangv.sorcicubespell.card.CardFaction;

public class FightHero extends FightHead {

	private PlayerFight player;
	
	public FightHero(PlayerFight player) {
		super(player.getFight(), player.getLocBase());
		this.player = player;
		this.updateStat();
	}
	
	@Override
	public boolean isFaction(CardFaction faction) {
		return true;
	}
	
	@Override
	public boolean isSelectable() {
		return true;
	}
	
	@Override
	public void updateStat() {
		String mana;
		if (player.canPlay())
			mana = Integer.toString(player.getMana());
		else {
			int value = player.getManaBoost();
			if (value < 0)
				mana = Integer.toString(player.getMana());
			else if (value > 0)
				mana = "+"+Integer.toString(player.getMana());
			else
				mana = "0";
		}
		this.setName("§c"+player.getHealth()+" \u2665 §b"+mana+" \u2756");
	}
	
	@Override
	public void setHealth(int health) {
		player.setHealth(health);
	}

	@Override
	public int getHealth() {
		return player.getHealth();
	}

	@Override
	public boolean hasIncitement() {
		return false;
	}
	
	public PlayerFight getPlayer() {
		return player;
	}

}
