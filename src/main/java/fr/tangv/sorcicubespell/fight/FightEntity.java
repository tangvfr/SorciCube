package fr.tangv.sorcicubespell.fight;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.scoreboard.CraftScoreboard;
import org.bukkit.event.EventHandler;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardSkin;
import fr.tangv.sorcicubespell.card.CardVisual;
import fr.tangv.sorcicubespell.util.RenderException;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.ScoreboardTeam;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R2.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;

public class FightEntity extends FightHead {

	private final EntityPlayer entityPlayer;
	private final EntityArmorStand entityStat;
	private final UUID uuid;
	private CardEntity card;
	private CardEntity lastCard;
	private CardSkin skin;
	private boolean isSend;
	private boolean attackIsPossible;
	private boolean attacked;
	
	public FightEntity(PlayerFight owner, Location loc) {
		super(owner, loc, 1.3D);
		this.card = null;
		this.lastCard = null;
		this.uuid = UUID.randomUUID();
		this.skin = null;
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		this.entityPlayer = new EntityPlayer(server, world, createProfil(""), new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0);
		//end init player
		this.isSend = false;
		this.attackIsPossible = false;
		this.attacked = false;
		this.entityStat = createArmorStand("", -0.2D);
	}
	
	@Override
	public void sendPacketForView(FightSpectator spectator) {
		super.sendPacketForView(spectator);
		spectator.sendPacket(new PacketPlayOutSpawnEntityLiving(entityStat));
		if (isSend) {
			//create team
			ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "GENE");
			team.setNameTagVisibility(EnumNameTagVisibility.NEVER);
			ArrayList<String> playerToAdd = new ArrayList<String>();
			playerToAdd.add(entityPlayer.getName());
			//send player
			spectator.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
			spectator.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
			spectator.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((loc.getYaw()*256F)/360F)));
			//send team
			spectator.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
			spectator.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
			spectator.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
		}
	}
	
	private void removePlayer() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		this.isSend = false;
	}
	
	private void repeatMove(int number) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(owner.getFight().getSorci(), new Runnable() {
			@Override
			public void run() {
				sendMovePlayer(loc.getDirection().clone().multiply(0.25));
				if (number > 1)
					repeatMove(number-1);
			}
		}, 2);
	}
	
	private void sendMovePlayer(Vector vec) {
		fight.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
				entityPlayer.getId(),
				(long) (vec.getX()*32)*128,
				(long) (vec.getY()*32)*128,
				(long) (vec.getZ()*32)*128,
				false
		));
	}
	
	private void spawnPlayer() {
		try {
			Field field = entityPlayer.getClass().getSuperclass().getDeclaredField("bS");
			field.setAccessible(true);
			field.set(entityPlayer, createProfil(""));
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
		}
		//create team
		ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "GENE");
		team.setNameTagVisibility(EnumNameTagVisibility.NEVER);
		ArrayList<String> playerToAdd = new ArrayList<String>();
		playerToAdd.add(entityPlayer.getName());
		//send player
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		fight.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
		fight.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((loc.getYaw()*256F)/360F)));
		//move player
		sendMovePlayer(loc.getDirection().clone().multiply(-1.5));
		repeatMove(6);
		//send team
		fight.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
		fight.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
		fight.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
		this.isSend = true;
	}
	
	private GameProfile skinToGameProfil(String name) {
		GameProfile gameProfile = new GameProfile(uuid, name);
		gameProfile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
		return gameProfile;
	}
	
	private GameProfile createProfil(String name) {
		if (skin != null)
			return skinToGameProfil(name);
		else
			return new GameProfile(uuid, name);
	}

	public void setStat(String stat) {
		super.sendFightHead(entityStat, stat, true);
	}
	
	public void setCard(CardEntity card) {
		this.card = card;
		this.lastCard = card;
		this.attackIsPossible = false;
		if (this.isSend)
			removePlayer();
		if (card != null) {
			this.skin = card.getSkin();
			this.setName(card.getCard().renderName());
			this.setAttackPossible(card.isExited());
			this.hideHead();
			this.updateStat();
			this.spawnPlayer();
			this.spawn();
		} else {
			this.setName("");
			this.setStat("");
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
	
	public boolean isAlreadyAttacked() {
		return attacked;
	}
	
	public void executingAction() {
		if (lastCard != null) {
			lastCard.excuteAction(this);
			if (card == null)
				lastCard = null;
		}
	}
	
	@Override
	public String getNameInChat() {
		return this.getName();
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
			this.setStat(CardVisual.renderStatCard(card.getCard()));
	}

	@Override
	public int getCounterAttack() {
		return card.isStunned() ? 0 : card.getAttack();
	}
	
	@Override
	public int damage(int damage) {
		if (!isDead()) {
			int counterAttack = getCounterAttack();
			if (this.card.hasIfAE())
				card.excutingIfAE();
			if (this.card.hasIfAG())
				card.excutingIfAG();
			if (!this.attacked) {
				this.attacked = true;
				if (this.card.hasIfAEO())
					card.excutingIfAEO();
				if (this.card.hasIfAGO())
					card.excutingIfAGO();
			}
			if (!card.isInvulnerability())
				setHealth(getHealth()-damage);
			return counterAttack;
		} else
			return 0;
	}
	
	@Override
	public void setHealth(int health) {
		card.setHealth(health);
		if (health <= 0)
			this.dead();
		updateStat();
	}

	@Override
	public int getHealth() {
		return card.getHealth();
	}
	
	public void addAttack(int attack) {
		setAttack(getAttack()+attack);
	}
	
	public void setAttack(int attack) {
		if (attack < 0)
			attack = 0;
		card.setAttack(attack);
		updateStat();
	}
	
	public int getAttack() {
		return card.getAttack();
	}
	
	@EventHandler
	public boolean isDead() {
		return this.card == null;
	}
	
	public void dead() {
		if (!isDead()) {
			if (card.hasActionDead()) {
				CardEntity card = this.card;
				this.setCard(null);
				card.excutingActionDead();
				this.lastCard = card;
				return;
			} else {
				owner.getFight().sendMessage(
						fight.getSorci().getMessage().getString("message_dead")
						.replace("{entity}", card.getCard().renderName())
						.replace("{owner}", owner.getNamePlayer())
				);
			}
		}
		this.setCard(null);
	}
	
	private void spawn() {
		this.attacked = false;
		if (this.card.hasActionSpawn()) {
			card.excutingActionSpawn();
		} else {
			owner.getFight().sendMessage(
					fight.getSorci().getMessage().getString("message_spawn")
					.replace("{entity}", card.getCard().renderName())
					.replace("{owner}", owner.getNamePlayer())
			);
		}
	}

	@Override
	public boolean hasIncitement() {
		return card != null && card.hasIncitement();
	}

}
