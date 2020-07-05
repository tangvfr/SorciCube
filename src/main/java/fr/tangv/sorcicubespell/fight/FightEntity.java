package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.WorldServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public class FightEntity {

	private Location loc;
	private EntityPlayer entityPlayer;
	private EntityArmorStand entityName;
	private EntityArmorStand entityStat;
	private EntityArmorStand entityHead;
	private PlayerConnection connection1;
	private PlayerConnection connection2;
	private GameProfile gameProfile;
	private Card card;
	
	public FightEntity(Fight fight, Location loc) {
		this.loc = loc;
		this.card = null;
		this.connection1 = ((CraftPlayer) fight.getPlayer1().getPlayer()).getHandle().playerConnection;
		this.connection2 = ((CraftPlayer) fight.getPlayer1().getPlayer()).getHandle().playerConnection;
		this.gameProfile = new GameProfile(UUID.randomUUID(), "§6Entity");
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", "").getBytes());
		Property property = new Property("textures", new String(encodedData));
	  	gameProfile.getProperties().put("textures", property);
	  	//create entity
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		this.entityPlayer = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
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
	
	private void sendPacket(Packet<?> packet) {
		connection1.sendPacket(packet);
		connection2.sendPacket(packet);
	}
	
	private void addPlayer() {
		sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		sendPacket(new PacketPlayOutSpawnEntityLiving(entityName));
	}
	
	private void removePlayer() {
		sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		sendPacket(new PacketPlayOutEntityDestroy(entityName.getId()));
	}
	
	public void reloadPlayer() {
		this.removePlayer();
		this.addPlayer();
	}
	
	private void addHead() {
		sendPacket(new PacketPlayOutSpawnEntityLiving(entityStat));
		sendPacket(new PacketPlayOutSpawnEntityLiving(entityHead));
	}
	
	private void removeHead() {
		sendPacket(new PacketPlayOutEntityDestroy(entityStat.getId()));
		sendPacket(new PacketPlayOutEntityDestroy(entityHead.getId()));
	}
	
	public void reloadHead() {
		this.removeHead();
		this.addHead();
	}
	
	private void setSkin(String url) {
		gameProfile.getProperties().removeAll("textures");
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", "").getBytes());
	  	gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	}
	
	public void setHead(CraftItemStack item) {
		entityHead.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item));
	}
	
	public void updateStat() {
		entityStat.setCustomName(CardRender.renderStatCard(card));
	}

	public void setCard(Card card) throws Exception {
		if (card == null) {
			removePlayer();
			removeHead();
		} else {
			if (card.getType() != CardType.ENTITY)
				throw new Exception("Card is not ENTITY !");
			String skin = "";
			for (CardFeature feature : card.getFeatures().listFeatures())
				if (feature.getType() == CardFeatureType.SKIN) {
					skin = feature.getValue().asString();
					break;
				}
			setSkin(skin);
			entityName.setCustomName(card.getName());
			setHead(null);
			updateStat();
			addPlayer();
			addHead();
		}
	}
	
	public Card getCard() {
		return card;
	}
	
	public boolean isTaken() {
		return card != null;
	}
	
}
