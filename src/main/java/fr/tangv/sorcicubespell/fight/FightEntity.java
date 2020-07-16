package fr.tangv.sorcicubespell.fight;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.CardEntity;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.util.RenderException;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class FightEntity extends FightHead {

	private EntityPlayer entityPlayer;
	private UUID uuid;
	private CardEntity card;
	private String skinUrl;
	private MinecraftServer server;
	private boolean isSend;
	
	public FightEntity(Fight fight, Location loc) {
		super(fight, loc);
		this.card = null;
		this.uuid = UUID.randomUUID();
		this.skinUrl = "";
		//create entity
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
		this.entityPlayer = new EntityPlayer(server, world, new GameProfile(uuid, ""), new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.isSend = false;
	}
	
	private void removePlayer() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		this.isSend = false;
	}
	
	private void changeProfil(String name) {
		try {
			GameProfile gameProfile = new GameProfile(uuid, name);
			gameProfile.getProperties().removeAll("textures");
			byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl).getBytes());
			gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
			Field field = entityPlayer.getClass().getSuperclass().getDeclaredField("bS");
			field.setAccessible(true);
			field.set(entityPlayer, gameProfile);
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
		}
	}

	public void setStat(String stat) {
		changeProfil(stat);
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		fight.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
		fight.sendPacket(
				new PacketPlayOutEntity.PacketPlayOutEntityLook(entityPlayer.getId(), (byte) ((loc.getYaw()*256F)/360F), (byte) 0, true)
			);
		fight.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((loc.getYaw()*256F)/360F)));
		this.isSend = true;
	}
	
	private void setSkin(String url) throws Exception {
		this.skinUrl = url;
	}
	
	public void setCard(CardEntity card) throws Exception {
		this.card = card;
		if (this.isSend)
			removePlayer();
		if (card != null) {
			setSkin(card.hasSkin() ? card.getSkin() : "");
			this.setName(card.getName());
			this.hideHead();
			this.updateStat();
		}
	}
	
	public CardEntity getCard() {
		return card;
	}
	
	@Override
	public boolean isFaction(CardFaction faction) {
		if (card == null)
			return false;
		else if (faction == CardFaction.BASIC)
			return true;
		else
			return faction == card.getCard().getFaction();
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
		if (attack < 0)
			attack = 0;
		card.setAttack(attack);
	}
	
	public int getAttack() {
		return card.getAttack();
	}

}
