package fr.tangv.sorcicubespell.fight;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.util.Cooldown;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Fight {

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
		this.titleBossBar = sorci.gertGuiConfig().getString("boss_bar.name");
		this.bossBar = Bukkit.createBossBar(titleBossBar, BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color")), BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
		this.round = -sorci.getParameter().getInt("cooldown_below_fight")-1;
		this.arena = sorci.getManagerFight().pickArena();
		//player1 start one
		if (Math.random() < 0.5) {
			this.player1 = new PlayerFight(this, preFight.getPlayer1(),
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(preFight.getPlayer1()).getDeck(preFight.getPlayer1DeckUse())),
					true
				);
			this.player2 = new PlayerFight(this, player2Arg,
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player2Arg).getDeck(preFight.getPlayer2DeckUse())),
					false
				);
		} else {
			this.player2 = new PlayerFight(this, preFight.getPlayer1(),
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(preFight.getPlayer1()).getDeck(preFight.getPlayer1DeckUse())),
					true
				);
			this.player1 = new PlayerFight(this, player2Arg,
					new FightDeck(sorci.getManagerPlayers().getPlayerFeature(player2Arg).getDeck(preFight.getPlayer2DeckUse())),
					false
				);
		}
		//init player
		player1.setEnemie(player2);
		player2.setEnemie(player1);
		player1.teleportToBase();
		player2.teleportToBase();
		sorci.getManagerFight().getPlayerFights().put(player1.getPlayer(), player1);
		sorci.getManagerFight().getPlayerFights().put(player2.getPlayer(), player2);
		this.bossBar.setVisible(false);
		this.bossBar.addPlayer(player1.getPlayer());
		this.bossBar.addPlayer(player2.getPlayer());
		//start
		cooldown.loop();
	}
	
	@SuppressWarnings("deprecation")
	public void update() {
		if (round < 0) {
			if (cooldown.update()) {
				String message;
				if (round == -1) {
					message = sorci.getMessage().getString("message_start_game");
					gameIsStart = true;
					bossBar.setVisible(true);
					nextRound();
				} else {
					message = sorci.getMessage().getString("message_below_start_game")
							.replace("{time}", Integer.toString(Math.abs(round+1)));
					round += 1;
				}
				player1.getPlayer().sendTitle("", message);
				player2.getPlayer().sendTitle("", message);
			}
		} else {
			if (cooldown.update()) {
				cooldown.stop();
				player1.getPlayer().sendTitle("", "");
				player2.getPlayer().sendTitle("", "");
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
	
	private void updatePlayer(PlayerFight player) {
		String messageActionBar = "";
		if (player.canPlay()) {
			int cardSelected = player.getCardSelect();
			if (cardSelected != -1) {
				Card card = player.getCardHand(cardSelected);
				messageActionBar = "§r§f> "+card.getName()+"§r§f <";
			}
		}
		player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
				new TextComponent(messageActionBar));
	}
	
	public void nextRound() {
		round += 1;
		cooldownRound.start();
		this.firstPlay = round%2 == 0;
		int mana = (round/2)+1;
		if (round == 1)
			mana = 2;
		PlayerFight player = this.firstPlay ? player1 : player2;
		player.setMana(mana+player.getManaBoost());
		player.setManaBoost(0);
		player.getEnemie().setMana(0);
		
		//send message nextRound
		
		//init inv etc
		player.initHotBar();
		player.getEnemie().initHotBar();
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
