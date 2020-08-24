package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class FightHero extends FightHead {
	
	public FightHero(PlayerFight owner) {
		super(owner, owner.getLocBase());
		this.updateStat();
	}
	
	public ItemStack renderToItem(boolean ally) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("  "+getName());
		lore.add("");
		lore.add("  "+owner.getFight().getSorci().getEnumConfig().getString("type.hero"));
		lore.add("");
		return ItemBuild.buildSkull(
				((CraftPlayer) owner.getPlayer()).getProfile(),
				ally ? 2 : 1,
				(ally ? "§2" : "§4")+owner.getPlayer().getName(),
				lore,
				false
			);
	}
	
	@Override
	public String getNameInChat() {
		return owner.getPlayer().getName();
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
	public int getCounterAttack() {
		return 0;
	}
	
	@Override
	public int damage(int damage) {
		setHealth(getHealth()-damage);
		return getCounterAttack();
	}

	@Override
	public boolean isDead() {
		return false;
	}

}
