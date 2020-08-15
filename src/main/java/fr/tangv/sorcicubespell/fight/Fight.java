package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.util.Cooldown;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;

public class Fight {
	
	//value static
	private final static double TOLERANCE = 4.6;
	private final static double TOLERANCE_ENTITY = 1.6;
	protected static final int max_health = 60;
	protected static final int start_health = 30;
	
	protected final int max_mana;
	private final SorciCubeSpell sorci;
	private final FightType fightType;
	private final PlayerFight player1;
	private final PlayerFight player2;
	private final FightArena arena;
	private int round;
	private final Cooldown cooldown;
	private final Cooldown cooldownEnd;
	private final Cooldown cooldownRound;
	private final String titleEnd;
	private volatile boolean firstPlay;
	private boolean isStart;
	private final BossBar bossBar;
	private final String titleBossBar;
	//end
	private volatile boolean isEnd;
	private volatile Player losser;
	private volatile Player winner;
	private volatile boolean isDeleted;
	
	public Fight(SorciCubeSpell sorci, PreFight preFight) throws Exception {
		this.sorci = sorci;
		this.max_mana = sorci.getParameter().getInt("max_mana");
		this.firstPlay = true;
		this.isStart = false;
		this.isDeleted = false;
		this.isEnd = false;
		this.losser = null;
		this.fightType = preFight.getFightType();
		this.cooldown = new Cooldown(1_000);
		this.cooldownRound = new Cooldown((long) sorci.getParameter().getInt("cooldown_one_round")*1000L);
		this.cooldownEnd = new Cooldown((long) sorci.getParameter().getInt("cooldown_end")*1000L);
		this.titleEnd = sorci.gertGuiConfig().getString("boss_bar.name_end");
		this.round = -sorci.getParameter().getInt("cooldown_below_fight")-1;
		this.arena = sorci.getManagerFight().pickArena();
		this.titleBossBar = sorci.gertGuiConfig().getString("boss_bar.name");
		this.bossBar = Bukkit.createBossBar(
				sorci.gertGuiConfig().getString("boss_bar.name_arena").replace("{arena}", this.arena.getName()),
				BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color_arena")),
				BarStyle.SOLID, new BarFlag[0]
			);
		//player1 start one
		if (Math.random() < 0.5) {
			this.player1 = createPlayerFight(preFight.getPlayer1(), preFight.getPlayer1DeckUse(), true);
			this.player2 = createPlayerFight(preFight.getPlayer2(), preFight.getPlayer2DeckUse(), false);
		} else {
			this.player2 = createPlayerFight(preFight.getPlayer1(), preFight.getPlayer1DeckUse(), false);
			this.player1 = createPlayerFight(preFight.getPlayer2(), preFight.getPlayer2DeckUse(), true);
		}
		//init player
		player1.initFightHead();
		player2.initFightHead();
		player1.teleportToBase();
		player2.teleportToBase();
		player1.setEnemie(player2);
		player2.setEnemie(player1);
		player1.createScoreboard();
		player2.createScoreboard();
		player1.getHero().updateStat();
		player2.getHero().updateStat();
		sorci.getManagerFight().getPlayerFights().put(player1.getPlayer(), player1);
		sorci.getManagerFight().getPlayerFights().put(player2.getPlayer(), player2);
		this.bossBar.addPlayer(player1.getPlayer());
		this.bossBar.addPlayer(player2.getPlayer());
		//start
		cooldown.loop();
	}

	private PlayerFight createPlayerFight(Player player, int deck, boolean first) throws Exception {
		return new PlayerFight(
				this, 
				player,
				new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player).getDeck(deck)),
				first
			);
	}
	
	public void update() {
		if (!isEnd) {
			if (round < 0) {
				if (cooldown.update()) {
					if (round == -1) {
						isStart = true;
						bossBar.setTitle(titleBossBar);
						bossBar.setColor(BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color")));
						nextRound();
						cooldown.stop();
					} else {
						sendTitleToTwoPlayer(
								sorci.getMessage().getString("message_below_start_game")
								.replace("{time}", Integer.toString(Math.abs(round+1)))
							);
						round += 1;
					}
				}
			} else {
				if (cooldownRound.update()) {
					nextRound();
					return;
				}
				//bossbar
				bossBar.setTitle(titleBossBar
						.replace("{time}", sorci.formatTime(cooldownRound.getTimeRemaining()))
						.replace("{round}", Integer.toString(round+1))
					);
				bossBar.setProgress(cooldownRound.getProgess());
				//player
				this.updatePlayer(player1);
				this.updatePlayer(player2);
			}
		} else {
			if (cooldownEnd.update()) {
				if (losser.isOnline())
					sorci.sendPlayerToServer(losser, sorci.getNameServerLobby());
				if (winner.isOnline())
					sorci.sendPlayerToServer(winner, sorci.getNameServerLobby());
				this.isDeleted = true;
			} else {
				bossBar.setTitle(titleEnd.replace("{time}", sorci.formatTime(cooldownEnd.getTimeRemaining())));
				bossBar.setProgress(cooldownEnd.getProgess());
			}
		}
	}
	
	private boolean locEntityInTolerance(Location locPos, Location locBase) {
		Location loc = locBase.clone();
		loc.setY(locPos.getY());
		if (loc.distance(locPos) <= TOLERANCE_ENTITY)
			return (Math.abs(locPos.getY()-(locBase.getY()+1D)) <= TOLERANCE_ENTITY);
		return false;
	}
	
	public FightCible getCibleForBlock(Block block, boolean first) {
		Location loc = block.getLocation();
		Location[] firstEntity = arena.getFirstEntity();
		Location[] secondEntity = arena.getSecondEntity();
		if (arena.getWorld().equals(block.getWorld())) {
			if (arena.getFirstBase().distance(block.getLocation()) <= TOLERANCE) {
				return first ? FightCible.HERO_ALLY : FightCible.HERO_ENEMIE;
				//hero first
			} else if (arena.getSecondBase().distance(block.getLocation()) <= TOLERANCE) {
				return !first ? FightCible.HERO_ALLY : FightCible.HERO_ENEMIE;
				//hero second
			} else 
			//first entity	
			if (locEntityInTolerance(loc, firstEntity[0])) {
				return first ? FightCible.ENTITY_1_ALLY : FightCible.ENTITY_1_ENEMIE;
				//entity first 1
			} else if (locEntityInTolerance(loc, firstEntity[1])) {
				return first ? FightCible.ENTITY_2_ALLY : FightCible.ENTITY_2_ENEMIE;
				//entity first 2
			} else if (locEntityInTolerance(loc, firstEntity[2])) {
				return first ? FightCible.ENTITY_3_ALLY : FightCible.ENTITY_3_ENEMIE;
				//entity first 3
			} else if (locEntityInTolerance(loc, firstEntity[3])) {
				return first ? FightCible.ENTITY_4_ALLY : FightCible.ENTITY_4_ENEMIE;
				//entity first 4
			} else if (locEntityInTolerance(loc, firstEntity[4])) {
				return first ? FightCible.ENTITY_5_ALLY : FightCible.ENTITY_5_ENEMIE;
				//entity first 5
			} else 
			//second entity
			if (locEntityInTolerance(loc, secondEntity[0])) {
				return !first ? FightCible.ENTITY_1_ALLY : FightCible.ENTITY_1_ENEMIE;
				//entity second 1
			} else if (locEntityInTolerance(loc, secondEntity[1])) {
				return !first ? FightCible.ENTITY_2_ALLY : FightCible.ENTITY_2_ENEMIE;
				//entity second 2
			} else if (locEntityInTolerance(loc, secondEntity[2])) {
				return !first ? FightCible.ENTITY_3_ALLY : FightCible.ENTITY_3_ENEMIE;
				//entity second 3
			} else if (locEntityInTolerance(loc, secondEntity[3])) {
				return !first ? FightCible.ENTITY_4_ALLY : FightCible.ENTITY_4_ENEMIE;
				//entity second 4
			} else if (locEntityInTolerance(loc, secondEntity[4])) {
				return !first ? FightCible.ENTITY_5_ALLY : FightCible.ENTITY_5_ENEMIE;
				//entity second 5
			} else {
				return null;
				//nothing
			}
		}
		return null;
	}
	
	public ItemStack renderCard(Card card) {
		return CardRender.cardToItem(card, sorci);
	}
	
	public void sendPacket(Packet<?> packet) {
		player1.sendPacket(packet);
		player2.sendPacket(packet);
	}
	
	public IChatBaseComponent toIChatBaseComposent(String text) {
		return ChatSerializer.a("{\"text\": \""+text+"\"}");
	}
	
	public void sendTitleToTwoPlayer(String message) {
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE,
				toIChatBaseComposent(""),
				0, 6, 0));
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				toIChatBaseComposent(message),
				0, 6, 0));
	}
	
	public void alertPlayer(Player player, String message) {
		player.getPlayer().sendMessage(message);
		PlayerFight.sendPacket(player, new PacketPlayOutTitle(EnumTitleAction.TITLE,
				toIChatBaseComposent(""),
				0, 6, 0));
		PlayerFight.sendPacket(player, new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				toIChatBaseComposent(message),
				0, 6, 0));
	}
	
	private void updatePlayer(PlayerFight player) {
		String messageActionBar = "";
		if (player.canPlay()) {
			int cardSelected = player.getCardSelect();
			if (cardSelected != -1) {
				Card card = player.getCardHand(cardSelected);
				messageActionBar = 
						CardRender.renderManaCard(card)+"§r §d> "+card.getName()+"§r§d < "+
						(card.getType() == CardType.ENTITY ? CardRender.renderStatCard(card) : CardRender.renderManaCard(card));
			}
			player.getPlayer().setExp(1F);
		} else {
			player.getPlayer().setExp(0F);
		}
		player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
				new TextComponent(messageActionBar));
		player.getPlayer().setLevel(player.getMana());
	}
	
	public void nextRound() {
		round += 1;
		cooldownRound.start();
		this.firstPlay = round%2 == 0;
		int mana = (round/2)+1;
		if (round == 1)
			mana = 2;
		if (mana > max_mana)
			mana = max_mana;
		player1.getPlayer().closeInventory();
		player2.getPlayer().closeInventory();
		PlayerFight player = this.firstPlay ? player1 : player2;
		player.setMana(mana+player.getManaBoost());
		player.setManaBoost(0);
		player.pickCard(1);
		//enemie
		player.getEnemie().setMana(0);
		player.getEnemie().setCardSelect(-1);
		//message
		sendTitleToTwoPlayer(
				sorci.getMessage().getString("message_round")
				.replace("{round}", Integer.toString(round+1))
			);
		//init inv
		player.initHotBar();
		player.getEnemie().initHotBar();
		//init view
		player.setEntityAttack(null);
		player.nextRoundFightEntity();
		player.resetEntityAttackPossible();
		player.showEntityAttackPossible();
	}
	
	//for end
	public boolean isEnd() {
		return isEnd;
	}
	
	public void end(Player losser) {
		end(losser, player1.isPlayer(losser) ? player2.getPlayer() : player1.getPlayer());
	}
	
	private void end(Player losser, Player winner) {
		this.losser = losser;
		this.winner = winner;
		this.cooldownEnd.start();
		bossBar.setColor(BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color_end")));
		bossBar.setTitle(titleEnd.replace("{time}", sorci.formatTime(cooldownEnd.getTimeRemaining())));
		bossBar.setProgress(cooldownEnd.getProgess());
		this.isEnd = true;
		if (losser.isOnline()) {
			alertPlayer(losser, sorci.getMessage().getString("message_losser"));
			losser.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent(""));
		}
		if (winner.isOnline()) {
			alertPlayer(winner, sorci.getMessage().getString("message_winner"));
			winner.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent(""));
		}
	}
	
	//geting seting
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public FightType getFightType() {
		return fightType;
	}
	
	public boolean isStart() {
		return isStart;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public int getRound() {
		return round;
	}
	
	public boolean getFirstPlay() {
		return firstPlay;
	}
	
	public FightArena getArena() {
		return arena;
	}
	
	public PlayerFight getPlayer1() {
		return player1;
	}

	public PlayerFight getPlayer2() {
		return player2;
	}
	
}