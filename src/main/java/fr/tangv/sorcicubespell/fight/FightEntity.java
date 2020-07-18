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
	private boolean isSend;
	
	public FightEntity(Fight fight, Location loc) {
		super(fight, loc);
		this.card = null;
		this.uuid = UUID.randomUUID();
		this.skinUrl = "http://textures.minecraft.net/texture/1a53379ca2eec3d4a0e0c7496b38e41f9c20fb59131a7c0074bd9bee5f230ee3";
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		this.entityPlayer = new EntityPlayer(server, world, createProfil("Default"), new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw()/2, loc.getPitch()/2);
		this.isSend = false;
	}
	
	private void removePlayer() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		this.isSend = false;
	}
	
	private GameProfile createProfil(String name) {
		GameProfile gameProfile = new GameProfile(uuid, name);
		byte[] encodedData = Base64.encodeBase64(("{\"textures\":{\"SKIN\":{\"url\":\""+skinUrl+"\"}}}").getBytes());
		gameProfile.getProperties().clear();
		gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		return gameProfile;
		/*
		 * id = 1563811155 // is upload before on this site
		 * https://mineskin.org/1563811155
		 * https://api.mineskin.org/get/id/1563811155/
		 * https://api.mineskin.org/render/1563811155/head
		 * https://api.mineskin.org/render/1563811155/skin
		 * and show skin url sirect [img1 | img2] on logi
		 */
	}
	
	private void changeProfil(String name) {
		try {
			Field field = entityPlayer.getClass().getSuperclass().getDeclaredField("bS");
			field.setAccessible(true);
			field.set(entityPlayer, createProfil(name));
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
		}
	}

	public void setStat(String stat) {
		if (this.isSend)
			removePlayer();
		changeProfil(stat);
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		fight.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
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
