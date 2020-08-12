package fr.tangv.sorcicubespell.fight;

import fr.tangv.sorcicubespell.card.CardFaction;

public class FightHero extends FightHead {
	
	public FightHero(PlayerFight owner) {
		super(owner, owner.getLocBase());
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
		if (owner.canPlay())
			mana = Integer.toString(owner.getMana())+" \u2756";
		else {
			int value = owner.getManaBoost();
			if (value < 0)
				mana = Integer.toString(owner.getManaBoost());
			else if (value > 0)
				mana = "+"+Integer.toString(owner.getManaBoost());
			else
				mana = "0";
			mana += " \u21ea";
		}
		this.setName("§c"+owner.getHealth()+" \u2665 §b"+mana);
	}
	
	@Override
	public void setHealth(int health) {
		owner.setHealth(health);
	}

	@Override
	public int getHealth() {
		return owner.getHealth();
	}

	@Override
	public boolean hasIncitement() {
		return false;
	}

	@Override
	public int damage(int damage) {
		setHealth(getHealth()-damage);
		return 0;
	}

	@Override
	public boolean isDead() {
		return false;
	}

}
