package fr.tangv.sorcicubespell.fight;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
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

	private final static int MAX_MANA_ROUND = 12;
	private SorciCubeSpell sorci;
	private FightType fightType;
	private PlayerFight player1;
	private PlayerFight player2;
	private FightArena arena;
	private int round;
	private Cooldown cooldown;
	private Cooldown cooldownRound;
	private boolean firstPlay;
	private boolean gameIsStart;
	private BossBar bossBar;
	private String titleBossBar;
	private ConcurrentHashMap<UUID, Card> hashCards;
	//end
	private boolean isEnd;
	private Player losser;
	
	public Fight(SorciCubeSpell sorci, PreFight preFight, Player player2Arg) throws Exception {
		this.sorci = sorci;
		this.firstPlay = true;
		this.gameIsStart = false;
		this.isEnd = false;
		this.losser = null;
		this.fightType = preFight.getFightType();
		this.cooldown = new Cooldown(1_000);
		this.cooldownRound = new Cooldown((long) sorci.getParameter().getInt("cooldown_one_round")*1000L);
		this.round = -sorci.getParameter().getInt("cooldown_below_fight")-1;
		this.arena = sorci.getManagerFight().pickArena();
		this.hashCards = new ConcurrentHashMap<UUID, Card>(sorci.getManagerCards().getCarts());
		this.titleBossBar = sorci.gertGuiConfig().getString("boss_bar.name");
		this.bossBar = Bukkit.createBossBar(
				sorci.gertGuiConfig().getString("boss_bar.name_arena").replace("{arena}", this.arena.getName()),
				BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color_arena")),
				BarStyle.SOLID, new BarFlag[0]
			);
		//player1 start one
		if (Math.random() < 0.5) {
			this.player1 = createPlayerFight(preFight.getPlayer1(), preFight.getPlayer1DeckUse(), true);
			this.player2 = createPlayerFight(player2Arg, preFight.getPlayer2DeckUse(), false);
		} else {
			this.player2 = createPlayerFight(preFight.getPlayer1(), preFight.getPlayer1DeckUse(), false);
			this.player1 = createPlayerFight(player2Arg, preFight.getPlayer2DeckUse(), true);
		}
		//init player
		player1.teleportToBase();
		player2.teleportToBase();
		player1.setEnemie(player2);
		player2.setEnemie(player1);
		player1.createScoreboard();
		player2.createScoreboard();
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
		if (round < 0) {
			if (cooldown.update()) {
				if (round == -1) {
					gameIsStart = true;
					bossBar.setTitle(titleBossBar);
					bossBar.setColor(BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color")));
					nextRound();
				} else {
					sendTitleToPlayer(
							sorci.getMessage().getString("message_below_start_game")
							.replace("{time}", Integer.toString(Math.abs(round+1)))
						);
					round += 1;
				}
			}
		} else {
			if (cooldown.update()) {
				cooldown.stop();
			}
			if (cooldownRound.update()) {
				nextRound();
				return;
			}
			bossBar.setTitle(titleBossBar
					.replace("{time}", sorci.formatTime(cooldownRound.getTimeRemaining()))
					.replace("{round}", Integer.toString(round+1))
				);
			bossBar.setProgress(cooldownRound.getProgess());
			this.updatePlayer(player1);
			this.updatePlayer(player2);
			//for test
			if (round >= 4)
				setEnd(player1.getPlayer());
			//end for test
		}
	}
	
	public ItemStack renderCard(Card card) {
		return CardRender.cardToItem(card, sorci, hashCards);
	}
	
	public void sendPacket(Packet<?> packet) {
		player1.sendPacket(packet);
		player2.sendPacket(packet);
	}
	
	public IChatBaseComponent toIChatBaseComposent(String text) {
		return ChatSerializer.a("{\"text\": \""+text+"\"}");
	}
	
	public void sendTitleToPlayer(String message) {
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE,
				toIChatBaseComposent(""),
				0, 6, 0));
		sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
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
		//scoreboard
		
	}
	
	public void nextRound() {
		round += 1;
		cooldownRound.start();
		this.firstPlay = round%2 == 0;
		int mana = (round/2)+1;
		if (round == 1)
			mana = 2;
		if (mana > MAX_MANA_ROUND)
			mana = MAX_MANA_ROUND;
		PlayerFight player = this.firstPlay ? player1 : player2;
		player.setMana(mana+player.getManaBoost());
		player.setManaBoost(0);
		player.pickCard(1);
		//enemie
		player.getEnemie().setMana(0);
		player.getEnemie().setCardSelect(-1);
		//and reset cible (entity head)
		//message
		sendTitleToPlayer(
				sorci.getMessage().getString("message_round")
				.replace("{round}", Integer.toString(round+1))
			);
		//init inv
		player.initHotBar();
		player.getEnemie().initHotBar();
		//for test
		player1.setHealth(player1.getHealth()-4);
		//end for test
	}
	
	//for end
	public void setEnd(Player losser) {
		this.losser = losser;
		this.isEnd = true;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	
	public void end() {
		end(losser);
	}
	
	//player is player loss
	public void end(Player losser) {
		end(losser, player1.isPlayer(losser) ? player2.getPlayer() : player1.getPlayer());
	}
	
	private void end(Player losser, Player winner) {
		setEnd(losser);
		if (losser.isOnline()) {
			
			sorci.sendPlayerToServer(losser, sorci.getNameServerLobby());
		}
		if (winner.isOnline()) {
			
			sorci.sendPlayerToServer(winner, sorci.getNameServerLobby());
		}
	}
	
	//geting seting
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public FightType getFightType() {
		return fightType;
	}
	
	public ConcurrentHashMap<UUID, Card> getHashCards() {
		return hashCards;
	}
	
	public boolean gameIsStart() {
		return gameIsStart;
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
