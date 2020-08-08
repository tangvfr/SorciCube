package fr.tangv.sorcicubespell.fight;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.CardEntity;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardSkin;
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
	private CardSkin skin;
	private boolean isSend;
	private boolean attackIsPossible;
	
	public FightEntity(PlayerFight owner, Location loc) {
		super(owner, loc);
		this.card = null;
		this.uuid = UUID.randomUUID();
		this.skin = null;
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		this.entityPlayer = new EntityPlayer(server, world, createProfil("Default"), new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw()/2, loc.getPitch()/2);
		this.isSend = false;
		this.attackIsPossible = false;
	}
	
	private void removePlayer() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		this.isSend = false;
	}
	
	private GameProfile skinToGameProfil(String name) {
		GameProfile gameProfile = new GameProfile(uuid, name);
		gameProfile.getProperties().clear();
		if (skin.isLastVersion())
			gameProfile.getProperties().put("textures", new Property("textures", skin.getTexture()));
		else
			gameProfile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
		return gameProfile;
	}
	
	private GameProfile createProfil(String name) {
		if (skin != null)
			return skinToGameProfil(name);
		else
			return new GameProfile(uuid, name);
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
	
	public void setCard(CardEntity card) {
		this.card = card;
		this.attackIsPossible = false;
		if (this.isSend)
			removePlayer();
		if (card != null) {
			this.skin = card.getSkin();
			this.setName(card.getName());
			this.hideHead();
			this.updateStat();
		}
	}
	
	public boolean attackIsPossible() {
		return attackIsPossible;
	}
	
	public void setAttackPossible(boolean attackPossible) {
		this.attackIsPossible = attackPossible;
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
		if (!isDead())
			this.setStat(CardRender.renderStatCard(card.getCard()));
	}

	@Override
	public int damage(int damage) {
		int cAttack = card.isStunned() ? 0 : card.getAttack();
		if (!card.isInvulnerability())
			setHealth(getHealth()-damage);
		return cAttack;
	}
	
	@Override
	public void setHealth(int health) {
		card.setHealth(health);
		if (health <= 0)
			this.dead();
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
	
	public boolean isDead() {
		return this.card == null;
	}
	
	public void dead() {
		this.setCard(null);
	}

	@Override
	public boolean hasIncitement() {
		return card != null && card.hasIncitement();
	}

}
