package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.util.ItemBuild;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public class FightHero extends FightHead {
	
	private EntityArmorStand armorFeaturesPlayer;
	
	public FightHero(PlayerFight owner) {
		super(owner, owner.getLocBase(), 1.5D);
		this.updateStat();
		String name = owner.getFight().getSorci().getParameter().getString("format_level");
		FightData data = owner.fight.getFightData();
		name = (data.getPlayerUUID1().equals(owner.getUUID()))
		?
			name.replace("{level}", Byte.toString(data.getLevelPlayer1()))
			.replace("{faction}", owner.getFight().getSorci().getEnumTool().factionToString(data.getFactionDeckPlayer1()))
		:
			name.replace("{level}", Byte.toString(data.getLevelPlayer2()))
			.replace("{faction}", owner.getFight().getSorci().getEnumTool().factionToString(data.getFactionDeckPlayer2()))
		;
		this.armorFeaturesPlayer = this.createArmorStand(name, 0.3D);
	}
	
	@Override
	public void sendPacketForView(FightSpectator spectator) {
		super.sendPacketForView(spectator);
		spectator.sendPacket(new PacketPlayOutSpawnEntityLiving(armorFeaturesPlayer));
	}
	
	public ItemStack renderToItem(boolean ally) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("  "+getName());
		lore.add("");
		lore.add("  "+owner.getFight().getSorci().getEnumConfig().getString("type.hero"));
		lore.add("");
		return ItemBuild.buildSkull(
				owner.getProfilePlayer(),
				ally ? 2 : 1,
				(ally ? "§2" : "§4")+owner.getNamePlayer(),
				lore,
				false
			);
	}
	
	@Override
	public String getNameInChat() {
		return owner.getNamePlayer();
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
