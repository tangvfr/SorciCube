package fr.tangv.sorcicubespell.fight;

import java.io.IOException;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.fight.FightCible;
import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightDeck;
import fr.tangv.sorcicubecore.fight.FightStat;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.util.Cooldown;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.util.Config;
import net.minecraft.server.v1_9_R2.Packet;

public class Fight {
	
	//value static
	private final static double TOLERANCE = 4.6;
	private final static double TOLERANCE_ENTITY = 1.6;
	protected final static int MAX_HEALTH = 60;
	protected final static int START_HEALTH = 30;
	
	private final FightData fightData;
	private final SorciCubeSpell sorci;
	private final PlayerFight player1;
	private final PlayerFight player2;
	private final FightArena arena;
	private int round;
	protected final long waitView;
	private final Cooldown cooldown;
	private final Cooldown cooldownEnd;
	private final Cooldown cooldownRound;
	private boolean firstPlay;
	private boolean isStart;
	private final BossBar bossBar;
	private final Vector<FightSpectator> spectators;
	private boolean init;
	//end
	private boolean isEnd;
	private boolean isDeleted;
	
	public Fight(SorciCubeSpell sorci, PreFight preFight) throws Exception {
		this.fightData = preFight.getFightData();
		this.sorci = sorci;
		this.init = false;
		this.firstPlay = true;
		this.isStart = false;
		this.isDeleted = false;
		this.isEnd = false;
		this.spectators = new Vector<FightSpectator>();
		this.cooldown = new Cooldown(1_000);
		this.cooldownRound = new Cooldown((long) sorci.getParameter().getInt("cooldown_one_round")*1000L);
		this.cooldownEnd = new Cooldown((long) sorci.getParameter().getInt("cooldown_end")*1000L);
		this.round = -sorci.getParameter().getInt("cooldown_before_fight")-1;
		this.waitView = sorci.getParameter().getInt("wait_view_fight");
		this.arena = sorci.getManagerFight().pickArena();
		this.bossBar = Bukkit.createBossBar(
				sorci.getGuiConfig().getString("boss_bar.name_arena").replace("{arena}", this.arena.getName()),
				BarColor.valueOf(sorci.getGuiConfig().getString("boss_bar.color_arena")),
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
		player1.setEnemie(player2);
		player2.setEnemie(player1);
		player1.initForViewFight();
		player2.initForViewFight();
		player1.getHero().updateStat();
		player2.getHero().updateStat();
		//start
		cooldown.loop();
		this.init = true;
	}
	
	private static interface ActionSpectator {
		public void action(FightSpectator spectator);
	}
	
	private void forEachSpectator(ActionSpectator action) {
		for (FightSpectator spectator : spectators)
			action.action(spectator);
	}
	
	protected BossBar getBossBar() {
		return bossBar;
	}
	
	public Vector<FightSpectator> getSpectators() {
		return spectators;
	}
	
	public void initForViewFight(FightSpectator spectator) {
		spectator.teleportToBase();
		spectator.showPlayer(player1);
		spectator.showPlayer(player2);
		spectator.createScoreboard();
		spectator.addInBossBar();
	}
	
	public void sendPacketForViewFight(FightSpectator spectator) {
		player1.getHero().sendPacketForView(spectator);
		player2.getHero().sendPacketForView(spectator);
		for (int i = 0; i < player1.getMaxEntity(); i++) {
			player1.getEntity(i).sendPacketForView(spectator);
			player2.getEntity(i).sendPacketForView(spectator);
		}
	}
	
	public void addSpectator(FightSpectator spectator) {
		spectators.add(spectator);
	}
	
	public void removeSpectator(FightSpectator spectator) {
		spectators.remove(spectator);
	}
	
	private PlayerFight createPlayerFight(Player player, int deck, boolean first) throws Exception {
		PlayerFeature playerFeature = sorci.getHandlerPlayers().getPlayer(player.getUniqueId());
		return new PlayerFight(
				this, 
				player,
				playerFeature,
				new FightDeck(playerFeature.getDeck(deck)),
				first
			);
	}
	
	public void update() throws IOException, ReponseRequestException, RequestException {
		if (!isEnd) {
			if (round < 0) {
				if (cooldown.update()) {
					if (round == -1) {
						isStart = true;
						bossBar.setTitle(ValueFight.V.titleBossBar);
						bossBar.setColor(ValueFight.V.titleBossBarColor);
						nextRound();
						cooldown.stop();
					} else {
						/*if (headIsInit == false && round2Max <= round)
							initHead();*/
						alertMessage(
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
				bossBar.setTitle(ValueFight.V.titleBossBar
						.replace("{time}", sorci.formatTime(cooldownRound.getTimeRemaining()))
						.replace("{round}", Integer.toString(round+1))
					);
				bossBar.setProgress(cooldownRound.getProgess());
				//player
				player1.updateDisplayPlayer();
				player2.updateDisplayPlayer();
			}
		} else {
			if (cooldownEnd.update()) {
				this.returnLobby();
				this.isDeleted = true;
			} else {
				bossBar.setTitle(ValueFight.V.titleEnd.replace("{time}", sorci.formatTime(cooldownEnd.getTimeRemaining())));
				bossBar.setProgress(cooldownEnd.getProgess());
			}
		}
		updatePacket();
	}
	
	private boolean locEntityInTolerance(Location locPos, Location locBase) {
		Location loc = locBase.clone();
		loc.setY(locPos.getY());
		if (loc.distance(locPos) <= TOLERANCE_ENTITY)
			return (Math.abs(locPos.getY()-(locBase.getY()+1D)) <= TOLERANCE_ENTITY);
		return false;
	}
	
	public FightCible getCibleForBlock(Block block, boolean first) {
		if (block == null) 
			return null;
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

	public void updatePacket() {
		player1.updatePacket();
		player2.updatePacket();
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.updatePacket();
		});
	}
	
	public void returnLobby() {
		player1.returnLobby();
		player2.returnLobby();
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.returnLobby();
		});
	}
	
	public void addHistoric(Card card, boolean first) {
		player1.addHistoric(card, first);
		player2.addHistoric(card, first);
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.addHistoric(card, first);
		});
	}
	
	public void closeInventory() {
		player1.closeInventory();
		player2.closeInventory();
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.closeInventory();
		});
	}
	
	public void updateViewLifes() {
		player1.updateViewLifes();
		player2.updateViewLifes();
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.updateViewLifes();
		});
	}
	
	public void alertMessage(String message) {
		player1.alert(message);
		player2.alert(message);
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.alert(message);
		});
	}
	
	public void sendMessage(String message) {
		player1.sendMessage(message);
		player2.sendMessage(message);
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.sendMessage(message);
		});
	}
	
	public void sendMessageSpectator(String message) {
		//spetator
		forEachSpectator((FightSpectator spectator) -> {
			spectator.sendMessage(message);
		});
	}
	
	public void sendPacket(Packet<?> packet) {
		if (this.init) {
			player1.sendPacket(packet);
			player2.sendPacket(packet);
			//spetator
			forEachSpectator((FightSpectator spectator) -> {
				spectator.sendPacket(packet);
			});
		}
	}
	
	public void nextRound() throws IOException, ReponseRequestException, RequestException {
		round += 1;
		cooldownRound.start();
		this.firstPlay = round%2 == 0;
		int mana = ((round+1)/2)+ValueFight.V.startMana;
		if (mana > ValueFight.V.maxMana)
			mana = ValueFight.V.maxMana;
		this.closeInventory();
		PlayerFight player = this.firstPlay ? player1 : player2;
		player.setMana(mana+player.getManaBoost());
		player.setManaBoost(0);
		player.pickCard(1);
		player.setAlreadySwap(false);
		//enemie
		player.getEnemie().setMana(0);
		player.getEnemie().setCardSelect(-1);
		player.getEnemie().nextRoundFightEntity();
		//message
		this.alertMessage(
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
		//afk
		player.addRoundAFK();
		player.getEnemie().addRoundAFK();
		player.playSound(Sound.BLOCK_NOTE_HARP, 1F, 1.5F);
	}
	
	//for end
	public boolean isEnd() {
		return isEnd;
	}
	
	public void end(PlayerFight losser) throws IOException, ReponseRequestException, RequestException {
		if (player1 == losser)
			end(player1, player2);
		else
			end(player2, player1);
	}
	
	private void endReward(Config lc, PlayerFight player, int money, int exp) throws IOException, ReponseRequestException, RequestException {
		PlayerFeature feature = player.getPlayerFeature();
		player.sendMessage(
				sorci.getMessage().getString("message_reward_end_game")
				.replace("{money}", Integer.toString(money))
				.replace("{experience}", Integer.toString(exp))
		);
		feature.addMoney(money);
		feature.addExperience(exp);
		if (!feature.isLevel((byte) lc.getInt("level_max"))) {
			int expNextLevel = lc.getInt("level_experience."+(feature.getLevel()+1)+".experience");
			if (feature.hasExperience(expNextLevel)) {
				feature.removeExperience(expNextLevel);
				feature.addLevel((byte) 1);
				int reward = lc.getInt("level_experience."+feature.getLevel()+".reward");
				feature.addMoney(reward);
				player.sendMessage(
						sorci.getMessage().getString("message_change_level")
						.replace("{level}", Byte.toString(feature.getLevel()))
						.replace("{money}", Integer.toString(reward))
				);
				player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			}
		}
		sorci.getHandlerPlayers().update(feature);
	}
	
	private void end(PlayerFight losser, PlayerFight winner) throws IOException, ReponseRequestException, RequestException {
		fightData.setStat(FightStat.END);
		sorci.getHandlerFightData().updateFightData(fightData);
		this.cooldownEnd.start();
		bossBar.setColor(ValueFight.V.titleEndColor);
		bossBar.setTitle(ValueFight.V.titleEnd.replace("{time}", sorci.formatTime(cooldownEnd.getTimeRemaining())));
		bossBar.setProgress(cooldownEnd.getProgess());
		this.isEnd = true;
		if (losser.isDead() && winner.isDead()) {
			winner.sendEndTitle(sorci.getMessage().getString("message_equality"));
			losser.sendEndTitle(sorci.getMessage().getString("message_equality"));
			Config lc = sorci.getLevelConfig();
			if (winner.isOnline())
				endReward(lc, winner, lc.getInt("money_equality"), lc.getInt("experience_equality"));
			if (losser.isOnline() && !losser.hasLossAFK())
				endReward(lc, losser, lc.getInt("money_equality"), lc.getInt("experience_equality"));
			//spectator
			forEachSpectator((FightSpectator spectator) -> {
				spectator.sendMessage(
						sorci.getMessage().getString("message_spectator_equality")
						.replace("{playerw}", winner.getNamePlayer())
						.replace("{playerl}", losser.getNamePlayer())
				);
			});
		} else {
			winner.sendEndTitle(sorci.getMessage().getString("message_winner"));
			losser.sendEndTitle(sorci.getMessage().getString("message_losser"));
			Config lc = sorci.getLevelConfig();
			if (winner.isOnline())
				endReward(lc, winner, lc.getInt("money_win"), lc.getInt("experience_win"));
			if (losser.isOnline() && !losser.hasLossAFK())
				endReward(lc, losser, lc.getInt("money_loss"), lc.getInt("experience_loss"));
			//spectator
			forEachSpectator((FightSpectator spectator) -> {
				spectator.sendMessage(
						sorci.getMessage().getString("message_spectator")
						.replace("{playerw}", winner.getNamePlayer())
						.replace("{playerl}", losser.getNamePlayer())
				);
			});
		}
	}
	
	//geting seting
	
	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public FightData getFightData() {
		return fightData;
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