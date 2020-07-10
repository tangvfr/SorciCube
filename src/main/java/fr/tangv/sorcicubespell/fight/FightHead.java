package fr.tangv.sorcicubespell.fight;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;

import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.WorldServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public abstract class FightHead {

	private Fight fight;
	private Location loc;
	private EntityArmorStand entityName;
	private EntityArmorStand entityStat;
	private EntityArmorStand entityHead;
	
	public FightHead(Fight fight, Location loc) {
		this.fight = fight;
		this.loc = loc;
		//create entity
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		this.entityStat = createArmorStand(world, "", 0D);
		this.entityName = createArmorStand(world, "", 0.25D);
		this.entityHead = createArmorStand(world, "", 1.0D);
	}
	
	private EntityArmorStand createArmorStand(WorldServer world, String name, double decal) {
		EntityArmorStand entity = new EntityArmorStand(world);
		entity.setGravity(false);
		entity.setBasePlate(false);
		entity.setInvulnerable(true);
		entity.setInvisible(true);
		entity.setCustomNameVisible(true);
		entity.setCustomName(name);
		entity.setLocation(loc.getX(), loc.getY()+decal, loc.getZ(), loc.getYaw(), loc.getPitch());
		return entity;
	}
	
	private void addHead() {
		fight.sendPacket(new PacketPlayOutSpawnEntityLiving(entityName));
		fight.sendPacket(new PacketPlayOutSpawnEntityLiving(entityStat));
		fight.sendPacket(new PacketPlayOutSpawnEntityLiving(entityHead));
	}
	
	private void removeHead() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityName.getId()));
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityStat.getId()));
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityHead.getId()));
	}
	
	public void reloadHead() {
		this.removeHead();
		this.addHead();
	}
	
	public void setHead(String head) {
		entityHead.setCustomName(head);
	}
	
	public void setHead(CraftItemStack item) {
		entityHead.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item));
	}
	
	public void setStat(String stat) {
		entityStat.setCustomName(stat);
	}
	
	public void setName(String name) {
		entityName.setCustomName(name);
	}
	
	public abstract boolean isSelectable();
	
	public abstract void addHeal();
	
	public abstract void removeHeal();
	
	public abstract void setHeal();
	
	public abstract void getHeal();
	
}
