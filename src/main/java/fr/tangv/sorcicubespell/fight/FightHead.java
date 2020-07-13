package fr.tangv.sorcicubespell.fight;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.CardFaction;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R2.WorldServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public abstract class FightHead {

	protected Fight fight;
	protected Location loc;
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
		entity.setLocation(loc.getX(), loc.getY()+decal, loc.getZ(), loc.getYaw(), loc.getPitch());
		entity.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
		sendHead(entity, name, false);
		return entity;
	}
	
	private void sendHeadEntity(EntityArmorStand entity) {
		fight.sendPacket(new PacketPlayOutEntityEquipment(entity.getId(), EnumItemSlot.HEAD, entity.getEquipment(EnumItemSlot.HEAD)));
	}
	
	private void sendHead(EntityArmorStand entity, String name, boolean already) {
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
		sendHeadEntity(entity);
	}
	
	public void showHead(ItemStack item) {
		entityHead.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item));
		sendHeadEntity(entityHead);
	}
	
	public void hideHead() {
		entityHead.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
		sendHeadEntity(entityHead);
	}
	
	public void setHead(String head) {
		sendHead(entityHead, head, true);
	}
	
	public void setStat(String stat) {
		sendHead(entityStat, stat, true);
	}
	
	public void setName(String name) {
		sendHead(entityName, name, true);
	}
	
	public abstract boolean isFaction(CardFaction faction);
	
	public abstract boolean isSelectable();
	
	public abstract void updateStat();
	
	public void addHealth(int health) {
		setHealth(getHealth()+health);
	}
	
	public void removeHealth(int health) {
		setHealth(getHealth()-health);
	}
	
	public abstract void setHealth(int health);
	
	public abstract int getHealth();
	
}
