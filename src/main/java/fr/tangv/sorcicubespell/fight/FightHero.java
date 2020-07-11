package fr.tangv.sorcicubespell.fight;

public class FightHero extends FightHead {

	private PlayerFight player;
	
	public FightHero(PlayerFight player) {
		super(player.getFight(), player.getLocBase());
		this.player = player;
	}

	@Override
	public boolean isSelectable() {
		return true;
	}
	
	@Override
	public void updateStat() {
		this.setStat("§c"+player.getHealth()+" \u2665");
	}
	
	@Override
	public void setHealth(int health) {
		player.setHealth(health);
	}

	@Override
	public int getHealth() {
		return player.getHealth();
	}

}
