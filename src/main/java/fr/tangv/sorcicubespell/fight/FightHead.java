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
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.WorldServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public abstract class FightHead {

	protected Fight fight;
	protected Location loc;
	protected WorldServer world;
	private EntityArmorStand entityName;
	private EntityArmorStand entityHead;
	private net.minecraft.server.v1_9_R2.ItemStack headItem;
	
	public FightHead(Fight fight, Location loc) {
		this.fight = fight;
		this.loc = loc;
		//create entity
		this.world = ((CraftWorld) loc.getWorld()).getHandle();
		this.entityName = createArmorStand("", 0.1D);
		this.entityHead = createArmorStand("", 1.1D);
	}
	
	protected EntityArmorStand createArmorStand(String name, double decal) {
		EntityArmorStand entity = new EntityArmorStand(this.world);
		entity.setGravity(false);
		entity.setBasePlate(false);
		entity.setInvulnerable(true);
		entity.setInvisible(true);
		entity.setLocation(loc.getX(), loc.getY()+decal, loc.getZ(), loc.getYaw(), loc.getPitch());
		sendHead(entity, name, false);
		return entity;
	}
	
	private void sendHeadEntity(EntityArmorStand entity) {
		fight.sendPacket(new PacketPlayOutEntityEquipment(entity.getId(), EnumItemSlot.HEAD, headItem));
	}
	
	protected void sendHead(EntityArmorStand entity, String name, boolean already) {
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
	
	public void rotateHead(float angle) {
		entityHead.yaw += angle;
		if (entityHead.yaw > 360.0F)
			entityHead.yaw -= 360.0F;
		fight.sendPacket(new PacketPlayOutEntityHeadRotation(entityHead, (byte) ((entityHead.yaw*256.0F)/360.0F)));
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
		sendHead(entityHead, head, true);
	}
	
	public void setName(String name) {
		sendHead(entityName, name, true);
	}
	
	public abstract boolean isFaction(CardFaction faction);
	
	public abstract boolean isSelectable();
	
	public abstract void updateStat();
	
	public abstract void setStat(String stat);
	
	public void addHealth(int health) {
		setHealth(getHealth()+health);
	}
	
	public void removeHealth(int health) {
		setHealth(getHealth()-health);
	}
	
	public abstract void setHealth(int health);
	
	public abstract int getHealth();
	
}