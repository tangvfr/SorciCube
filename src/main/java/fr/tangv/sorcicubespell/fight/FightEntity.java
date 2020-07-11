package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.CardEntity;
import fr.tangv.sorcicubespell.card.CardRender;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.WorldServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class FightEntity extends FightHead {

	private EntityPlayer entityPlayer;
	private GameProfile gameProfile;
	private CardEntity card;
	
	public FightEntity(Fight fight, Location loc) {
		super(fight, loc);
		this.card = null;
		this.gameProfile = new GameProfile(UUID.randomUUID(), "§6Entity");
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", "").getBytes());
		Property property = new Property("textures", new String(encodedData));
		gameProfile.getProperties().put("textures", property);
		//create entity
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		this.entityPlayer = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public void addPlayer() {
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
	}
	
	public void removePlayer() {
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
	}
	
	private void setSkin(String url) {
		gameProfile.getProperties().removeAll("textures");
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", "").getBytes());
	  	gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	}
	
	public void setCard(CardEntity card) throws Exception {
		if (this.card != null) {
			removePlayer();
			removeHead();
		}
		this.card = card;
		if (card != null) {
			setSkin(card.hasSkin() ? card.getSkin() : "");
			this.setName(card.getName());
			this.hideHead();
			this.updateStat();
			addPlayer();
			this.addHead();
		}
	}
	
	public CardEntity getCard() {
		return card;
	}
	
	@Override
	public boolean isSelectable() {
		return card != null;
	}

	@Override
	public void updateStat() {
		this.setStat(CardRender.renderStatCard(card.getCard()));
	}

	@Override
	public void setHealth(int health) {
		//add if heal < 0 is dead, add action 
		card.setHealth(health);
	}

	@Override
	public int getHealth() {
		return card.getHealth();
	}
	
	public void setAttack(int attack) {
		card.setAttack(attack);
	}
	
	public int getAttack() {
		return card.getAttack();
	}

}
