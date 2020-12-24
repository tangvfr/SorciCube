package fr.tangv.sorcicubespell.fight;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.CardFaction;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_9_R2.WorldServer;

public abstract class FightHead {

	protected final PlayerFight owner;
	protected final Fight fight;
	protected final Location loc;
	protected final WorldServer world;
	private final EntityArmorStand entityName;
	private final EntityArmorStand entityHead;
	private net.minecraft.server.v1_9_R2.ItemStack headItem;
	
	public FightHead(PlayerFight owner, Location loc, double headHeight) {
		this.fight = owner.getFight();
		this.owner = owner;
		this.loc = loc;
		//create entity
		this.world = ((CraftWorld) loc.getWorld()).getHandle();
		this.entityName = createArmorStand("", 0.1D);
		this.entityHead = createArmorStand("", headHeight);
	}
	
	protected EntityArmorStand createArmorStand(String name, double decal) {
		EntityArmorStand entity = new EntityArmorStand(this.world);
		entity.setGravity(false);
		entity.setBasePlate(false);
		entity.setInvulnerable(true);
		entity.setInvisible(true);
		entity.setLocation(loc.getX(), loc.getY()+decal, loc.getZ(), loc.getYaw(), loc.getPitch());
		sendFightHead(entity, name, false);
		return entity;
	}
	
	public void sendPacketForView(FightSpectator spectator) {
		spectator.sendPacket(new PacketPlayOutSpawnEntityLiving(entityName));
		spectator.sendPacket(new PacketPlayOutSpawnEntityLiving(entityHead));
		spectator.sendPacket(new PacketPlayOutEntityEquipment(entityHead.getId(), EnumItemSlot.HEAD, headItem));
	}
	
	private void sendHeadEntity(EntityArmorStand entity) {
		fight.sendPacket(new PacketPlayOutEntityEquipment(entity.getId(), EnumItemSlot.HEAD, headItem));
	}
	
	protected void sendFightHead(EntityArmorStand entity, String name, boolean already) {
		if (already)
			fight.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
		if (name.isEmpty()) {
			entity.setCustomNameVisible(false);
			entity.setCustomName(name);
		} else {
			entity.setCustomNameVisible(true);
			entity.setCustomName(name);
		}
		fight.sendPacket(new PacketPlayOutSpawnEntityLiving(entity));
	}
	
	public void showHead(ItemStack item) {
		headItem = CraftItemStack.asNMSCopy(item);
		sendHeadEntity(entityHead);
	}
	
	public void hideHead() {
		headItem = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
		sendHeadEntity(entityHead);
	}
	
	public void setHead(String head) {
		sendFightHead(entityHead, head, true);
		sendHeadEntity(entityHead);
	}
	
	public void setName(String name) {
		sendFightHead(entityName, name, true);
	}
	
	protected String getName() {
		return entityName.getCustomName();
	}
	
	public PlayerFight getOwner() {
		return owner;
	}
	
	public abstract String getNameInChat();
	
	public abstract boolean isFaction(CardFaction faction);
	
	public abstract boolean isSelectable();
	
	public abstract void updateStat();
	
	public abstract int getCounterAttack();
	
	public abstract int damage(int damage);
	
	public void addHealth(int health) {
		setHealth(getHealth()+health);
	}
	
	public abstract boolean isDead();
	
	protected abstract void setHealth(int health);
	
	public abstract int getHealth();
	
	public abstract boolean hasIncitement();
	
}
