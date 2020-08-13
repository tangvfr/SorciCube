package fr.tangv.sorcicubespell.fight;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.scoreboard.CraftScoreboard;
import org.bukkit.event.EventHandler;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardEntity;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardSkin;
import fr.tangv.sorcicubespell.util.RenderException;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PlayerInteractManager;
import net.minecraft.server.v1_9_R2.ScoreboardTeam;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R2.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam;

public class FightEntity extends FightHead {

	private final EntityPlayer entityPlayer;
	private final EntityArmorStand entityStat;
	private final UUID uuid;
	private volatile CardEntity card;
	private volatile CardSkin skin;
	private volatile boolean isSend;
	private volatile boolean attackIsPossible;
	private volatile boolean attacked;
	
	public FightEntity(PlayerFight owner, Location loc) {
		super(owner, loc);
		this.card = null;
		this.uuid = UUID.randomUUID();
		this.skin = null;
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		this.entityPlayer = new EntityPlayer(server, world, createProfil(""), new PlayerInteractManager(world));
		this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		//end init player
		this.isSend = false;
		this.attackIsPossible = false;
		this.attacked = false;
		this.entityStat = createArmorStand("", -0.2D);
	}
	
	private void removePlayer() {
		fight.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
		fight.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		this.isSend = false;
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
		fight.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((loc.getYaw()*256.0F)/360.0F)));
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
		super.sendHead(entityStat, stat, true);
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
		if (!isDead()) {
			String string = CardRender.renderStatCard(card.getCard());
			if (card.isImmobilization())
				string += "  §b\u2744";
			if (card.isStunned())
				string += "  §6\u0040";
			if (card.isInvulnerability())
				string += "  §d\u267e";
			this.setStat(string);
		}
	}

	@Override
	public int damage(int damage) {
		if (!isDead()) {
			int cAttack = card.isStunned() ? 0 : card.getAttack();
			if (this.card.hasIfAE())
				actionedCard(card.getIfAE(owner.getFight().getSorci()));
			if (this.card.hasIfAG())
				giveCard(card.getIfAG(owner.getFight().getSorci()));
			if (!this.attacked) {
				this.attacked = true;
				if (this.card.hasIfAEO())
					actionedCard(card.getIfAEO(owner.getFight().getSorci()));
				if (this.card.hasIfAGO())
					giveCard(card.getIfAGO(owner.getFight().getSorci()));
			}
			if (!card.isInvulnerability())
				setHealth(getHealth()-damage);
			return cAttack;
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
	
	private void actionedCard(Card card) {
		FightSpell.startActionSpell(owner, card.getFeatures(), 
				FightCible.randomFightHeadsForCible(owner, card.getCible(), card.getCibleFaction()));
	}
	
	private void giveCard(CardFeature feature) {
		FightSpell.startActionFeature(owner, feature, this);
	}
	
	public void dead() {
		if (!isDead() && card.hasActionDead())
			actionedCard(card.getActionDead(owner.getFight().getSorci()));
		this.setCard(null);
	}
	
	private void spawn() {
		this.attacked = false;
		if (this.card.hasActionSpawn())
			actionedCard(card.getActionSpawn(owner.getFight().getSorci()));
	}

	@Override
	public boolean hasIncitement() {
		return card != null && card.hasIncitement();
	}

}
