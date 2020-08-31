package fr.tangv.sorcicubespell.fight;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IScoreboardCriteria;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.Scoreboard;
import net.minecraft.server.v1_9_R2.ScoreboardObjective;
import net.minecraft.server.v1_9_R2.ScoreboardScore;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;

public class FightSpectator {

	//static
	
	private static final ConcurrentSet<Material> materialTransparent;
	private static final int distanceGetBlock = 100;
	
	static {
		materialTransparent = new ConcurrentSet<Material>();
		materialTransparent.add(Material.AIR);
		materialTransparent.add(Material.LAVA);
		materialTransparent.add(Material.WATER);
		materialTransparent.add(Material.PORTAL);
		materialTransparent.add(Material.ENDER_PORTAL);
		materialTransparent.add(Material.STATIONARY_LAVA);
		materialTransparent.add(Material.STATIONARY_WATER);
		materialTransparent.add(Material.CROPS);
		materialTransparent.add(Material.VINE);
		materialTransparent.add(Material.SUGAR_CANE_BLOCK);
		materialTransparent.add(Material.SAPLING);
		materialTransparent.add(Material.PUMPKIN_STEM);
		materialTransparent.add(Material.POTATO);
		materialTransparent.add(Material.MELON_STEM);
		materialTransparent.add(Material.WATER_LILY);
		materialTransparent.add(Material.DOUBLE_PLANT);
		materialTransparent.add(Material.LONG_GRASS);
		materialTransparent.add(Material.RED_ROSE);
		materialTransparent.add(Material.YELLOW_FLOWER);
		materialTransparent.add(Material.RED_MUSHROOM);
		materialTransparent.add(Material.BROWN_MUSHROOM);
		materialTransparent.add(Material.DEAD_BUSH);
		materialTransparent.add(Material.COCOA);
		materialTransparent.add(Material.CARROT);
		materialTransparent.add(Material.BEETROOT_BLOCK);
		materialTransparent.add(Material.CHORUS_FLOWER);
		materialTransparent.add(Material.CHORUS_PLANT);
		materialTransparent.add(Material.TRAP_DOOR);
		materialTransparent.add(Material.IRON_TRAPDOOR);
		materialTransparent.add(Material.STONE_PLATE);
		materialTransparent.add(Material.GOLD_PLATE);
		materialTransparent.add(Material.WOOD_PLATE);
		materialTransparent.add(Material.IRON_PLATE);
		materialTransparent.add(Material.STONE_BUTTON);
		materialTransparent.add(Material.WOOD_BUTTON);
		materialTransparent.add(Material.TORCH);
		materialTransparent.add(Material.REDSTONE_TORCH_OFF);
		materialTransparent.add(Material.REDSTONE_TORCH_ON);
		materialTransparent.add(Material.LEVER);
		materialTransparent.add(Material.LADDER);
		materialTransparent.add(Material.RAILS);
		materialTransparent.add(Material.ACTIVATOR_RAIL);
		materialTransparent.add(Material.DETECTOR_RAIL);
		materialTransparent.add(Material.POWERED_RAIL);
		materialTransparent.add(Material.STANDING_BANNER);
		materialTransparent.add(Material.WALL_BANNER);
		materialTransparent.add(Material.END_ROD);
		materialTransparent.add(Material.REDSTONE_WIRE);
		materialTransparent.add(Material.REDSTONE_COMPARATOR_OFF);
		materialTransparent.add(Material.REDSTONE_COMPARATOR_ON);
		materialTransparent.add(Material.DIODE_BLOCK_OFF);
		materialTransparent.add(Material.DIODE_BLOCK_ON);
		materialTransparent.add(Material.SIGN_POST);
		materialTransparent.add(Material.WALL_SIGN);
		materialTransparent.add(Material.SNOW);
		materialTransparent.add(Material.TRIPWIRE);
		materialTransparent.add(Material.TRIPWIRE_HOOK);
		materialTransparent.add(Material.WEB);
	}
	
	//dynamic
	
	protected final Fight fight;
	private final Inventory invHistoric;
	private final Inventory invViewEntity;
	private volatile Player player;
	private final Location locBase;
	private final String name;
	private final UUID uuid;
	private final GameProfile profile;
	protected final boolean first;
	private final Vector<Integer> invAutorized;
	
	//scoreboard
	private volatile String[] lastScoreMy;
	private volatile String[] lastScoreEnemie;
	private volatile Scoreboard sc;
	private volatile ScoreboardObjective scob;
	
	public FightSpectator(Fight fight, Player player, Location locBase, boolean first) {
		this.fight = fight;
		this.player = player;
		this.locBase = locBase;
		this.first = first;
		this.name = player.getName();
		this.uuid = player.getUniqueId();
		this.profile = ((CraftPlayer) player).getProfile();
		this.invHistoric = Bukkit.createInventory(player, 9, fight.getSorci().getGuiConfig().getString("gui_historic.name"));
		this.invViewEntity = Bukkit.createInventory(player, InventoryType.DISPENSER, fight.getSorci().getGuiConfig().getString("gui_view_entity.name"));
		this.invAutorized = new Vector<Integer>();
		addInventoryAutorized(invHistoric);
		addInventoryAutorized(invViewEntity);
	}
	
	public void newPlayer(Player player) {
		this.player = player;
		fight.initPacketForViewFight(this);
	}
	
	public void addInventoryAutorized(Inventory inv) {
		invAutorized.add(inv.hashCode());
	}
	
	public boolean inventoryIsAutorized(Inventory inventory) {
		return invAutorized.contains(inventory.hashCode());
	}
	
	public Fight getFight() {
		return fight;
	}
	
	private void sendScore(String name, String lastName, int scoreNumber) {
		if (lastName != null)
			sendPacket(new PacketPlayOutScoreboardScore(lastName)/*remove*/);
		ScoreboardScore score = new ScoreboardScore(this.sc, this.scob, name/*text display*/);
		score.setScore(scoreNumber);
		sendPacket(new PacketPlayOutScoreboardScore(score)/*change*/);
	}
	
	public void createScoreboard() {
		if (first)
			this.createScoreboard(fight.getPlayer1().getNamePlayer(), fight.getPlayer2().getNamePlayer(), fight.getPlayer1().getHealth(), fight.getPlayer2().getHealth());
		else
			this.createScoreboard(fight.getPlayer2().getNamePlayer(), fight.getPlayer1().getNamePlayer(), fight.getPlayer2().getHealth(), fight.getPlayer1().getHealth());
	}
	
	private void createScoreboard(String nameAlly, String nameEnemie, int healthAlly, int healthEnemie) {
		this.sc = new Scoreboard();
		this.scob = new ScoreboardObjective(sc, 
				fight.getSorci().getGuiConfig().getString("scoreboard.name")/*displayName*/,
				IScoreboardCriteria.b/*dummy*/
			);
		sendPacket(new PacketPlayOutScoreboardObjective(scob, 0/*0 create, 1 remmove, 2 update*/));
		sendPacket(new PacketPlayOutScoreboardDisplayObjective(1/*0 LIST, 1 SIDEBAR, 2 BELOW_NAME*/, scob));
		//init score
		this.lastScoreMy = healthToString(false, healthAlly);
		this.lastScoreEnemie = healthToString(true, healthEnemie);
		sendScore(" ", null, -1);
		sendScore("§7"+nameAlly+"§8:", null, -2);
		sendScore(this.lastScoreMy[0], null, -3);
		sendScore(this.lastScoreMy[1], null, -4);
		sendScore(this.lastScoreMy[2], null, -5);
		sendScore("   ", null, -6);
		sendScore("    ", null, -7);
		sendScore("     ", null, -8);
		sendScore("      ", null, -9);
		sendScore("       ", null, -10);
		sendScore("§7"+nameEnemie+"§8:", null, -11);
		sendScore(this.lastScoreEnemie[0], null, -12);
		sendScore(this.lastScoreEnemie[1], null, -13);
		sendScore(this.lastScoreEnemie[2], null, -14);
		sendScore("         ", null, -15);
		//update objective
		sendPacket(new PacketPlayOutScoreboardObjective(scob, 2/*0 create, 1 remmove, 2 update*/));
	}
	
	public void updateViewLifes() {
		if (first)
			this.updateViewLifes(fight.getPlayer1().getHealth(), fight.getPlayer2().getHealth());
		else
			this.updateViewLifes(fight.getPlayer2().getHealth(), fight.getPlayer1().getHealth());
	}
	
	private void updateViewLifes(int healthAlly, int healthEnemie) {
		String[] scoreMy = healthToString(false, healthAlly);
		String[] scoreEnemie = healthToString(true, healthEnemie);
		//update score
		sendScore(scoreMy[0], this.lastScoreMy[0], -3);
		sendScore(scoreMy[1], this.lastScoreMy[1], -4);
		sendScore(scoreMy[2], this.lastScoreMy[2], -5);
		sendScore(scoreEnemie[0], this.lastScoreEnemie[0], -12);
		sendScore(scoreEnemie[1], this.lastScoreEnemie[1], -13);
		sendScore(scoreEnemie[2], this.lastScoreEnemie[2], -14);
		//update objective
		sendPacket(new PacketPlayOutScoreboardObjective(scob, 2/*0 create, 1 remmove, 2 update*/));
		this.lastScoreMy = scoreMy;
		this.lastScoreEnemie = scoreEnemie;
	}
	
	private static String generatedChar(char c, int number) {
		String text = "";
		for (int i = 0; i < number; i++)
			text += c;
		return text;
	}
	
	private String[] healthToString(boolean enemie, int health) {
		String[] text = new String[3];
		String color;
		String colorOff;
		int number = health;
		if (number > 30) {
			number -= 30;
			colorOff = "§c";
			color = "§a";
		} else {
			colorOff = "§7";
			color = "§c";
		}
		char cara = '\u25A0';
		for (int i = 0; i < 3; i++) {
			int max = 10*(i+1);
			if (number <= max) {
				int off = max-number;
				if (off > 10)
					off = 10;
				text[i] = color+generatedChar(cara, 10-off)+colorOff+generatedChar(cara, off);
			} else {
				text[i] = color+generatedChar(cara, 10);
			}
		}
		String startColor = enemie ? "§r§8" : "§8";
		text[0] = startColor+"╔"+text[0]+"§8╗";
		text[1] = startColor+"╠"+text[1]+"§8╣";
		text[2] = startColor+"╚"+text[2]+"§8╝";
		return text;
		/*
		 *╔╗
		 *╠╣
		 *╚╝
		*/
	}
	
	public void setLevel(int level) {
		player.setLevel(level);
	}
	
	public void setExp(float exp) {
		player.setExp(exp);
	}
	
	public void showPlayer(FightSpectator spectator) {
		Bukkit.getScheduler().runTask(fight.getSorci(), new Runnable() {
			@Override
			public void run() {
				if (player != spectator.player && player.isOnline() && spectator.player.isOnline())
					player.showPlayer(spectator.player);
			}
		});
	}
	
	public void returnLobby() {
		if (player.isOnline())
			fight.getSorci().sendPlayerToServer(player, fight.getSorci().getNameServerLobby());
	}
	
	public void closeInventory() {
		if (player.isOnline())
			player.closeInventory();
	}
	
	public void openInventory(Inventory inv) {
		player.openInventory(inv);
	}
	
	public void openInvViewEntity(ItemStack item) {
		invViewEntity.setItem(4, item);
		player.openInventory(invViewEntity);
	}
	
	public Inventory getInvViewEntity() {
		return invViewEntity;
	}
	
	public void addHistoric(Card card, boolean first) {
		ItemStack item = CardRender.cardToItem(card, fight.getSorci(), (this.first == first) ? 2 : 1, false);
		for (int i = 0; i < 8; i++)
			invHistoric.setItem(i, invHistoric.getItem(i+1));
		invHistoric.setItem(8, item);
	}
	
	public void openInvHistoric() {
		player.openInventory(invHistoric);
	}
	
	public Inventory getInvHistoric() {
		return invHistoric;
	}
	
	public void updateInventory() {
		player.updateInventory();
	}
	
	public ItemStack getInvItem(int index) {
		return player.getPlayer().getInventory().getItem(index);
	}
	
	public void setInvItem(int index, ItemStack item) {
		player.getPlayer().getInventory().setItem(index, item);
	}
	
	public boolean isOnline() {
		return player.isOnline();
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Inventory getInvOpenTop() {
		return player.getPlayer().getOpenInventory().getTopInventory();
	}
	
	public boolean hasStickView() {
		return player.getInventory().getItemInMainHand().isSimilar(ValueFight.V.itemStickView);
	}
	
	public GameProfile getProfilePlayer() {
		return profile;
	}
	
	public String getNamePlayer() {
		return name;
	}
	
	public void addInBossBar() {
		fight.getBossBar().addPlayer(player);
	}
	
	public void removeInBossBar() {
		fight.getBossBar().removePlayer(player);
	}
	
	public FightCible getTargetCible() {
		return fight.getCibleForBlock(player.getTargetBlock(materialTransparent, distanceGetBlock), first);
	}
	
	private PlayerFight getAlly() {
		return (first ? fight.getPlayer1() : fight.getPlayer2());
	}
	
	private PlayerFight getEnemie() {
		return (first ? fight.getPlayer2() : fight.getPlayer1());
	}
	
	public FightHead getForCible(FightCible cible) {
		switch (cible) {
			case ENTITY_1_ALLY:
				return getAlly().getEntity(0);
			
			case ENTITY_2_ALLY:
				return getAlly().getEntity(1);
				
			case ENTITY_3_ALLY:
				return getAlly().getEntity(2);
				
			case ENTITY_4_ALLY:
				return getAlly().getEntity(3);
				
			case ENTITY_5_ALLY:
				return getAlly().getEntity(4);
				
			case HERO_ALLY:
				return getAlly().getHero();
				
			case ENTITY_1_ENEMIE:
				return getEnemie().getEntity(0);
			
			case ENTITY_2_ENEMIE:
				return getEnemie().getEntity(1);
				
			case ENTITY_3_ENEMIE:
				return getEnemie().getEntity(2);
				
			case ENTITY_4_ENEMIE:
				return getEnemie().getEntity(3);
				
			case ENTITY_5_ENEMIE:
				return getEnemie().getEntity(4);
				
			case HERO_ENEMIE:
				return getEnemie().getHero();
				
			default:
				return null;//not possible
		}
	}
	
	public void playSound(Sound sound, float volume, float pitch) {
		if (player.isOnline())
			player.playSound(player.getLocation(), sound, volume, pitch);
	}
	
	public void sendMessageInsufficientMana() {
		playSound(Sound.ENTITY_ENDERMEN_SCREAM, 1.0F, 1.5F);
		sendMessage(fight.getSorci().getMessage().getString("message_mana_insufficient"));
	}
	
	public static IChatBaseComponent toIChatBaseComposent(String text) {
		return ChatSerializer.a("{\"text\": \""+text+"\"}");
	}
	
	public void sendMessage(String message) {
		sendPacket(new PacketPlayOutChat(FightSpectator.toIChatBaseComposent(message), (byte) 0));
	}
	
	public void sendMessageActionBar(String message) {
		sendPacket(new PacketPlayOutChat(FightSpectator.toIChatBaseComposent(message), (byte) 2));
	}
	
	public void sendPacket(Packet<?> packet) {
		if (player.isOnline())
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void alert(String message) {
		sendMessage(message);
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE,
				FightSpectator.toIChatBaseComposent(""),
				0, 6, 0));
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				FightSpectator.toIChatBaseComposent(message.replace("\n", "")),
				0, 6, 0));
	}
	
	public Location getLocBase() {
		return this.locBase;
	}
	
	public boolean teleportToBase() {
		return player.teleport(this.locBase, TeleportCause.PLUGIN);
	}
	
	public boolean isFightPlayer() {
		return (this instanceof PlayerFight);
	}
	
}
