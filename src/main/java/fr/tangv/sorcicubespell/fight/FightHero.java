package fr.tangv.sorcicubespell.fight;

import fr.tangv.sorcicubespell.card.CardFaction;
import net.minecraft.server.v1_9_R2.EntityArmorStand;

public class FightHero extends FightHead {

	private PlayerFight player;
	private EntityArmorStand entityStat;
	
	public FightHero(PlayerFight player) {
		super(player.getFight(), player.getLocBase());
		this.player = player;
		this.entityStat = createArmorStand("", -0.15);
	}

	@Override
	public void setStat(String stat) {
		sendHead(entityStat, stat, true);
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
