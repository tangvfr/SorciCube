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
import fr.tangv.sorcicubecore.configs.Config;
import fr.tangv.sorcicubecore.fight.FightCible;
import fr.tangv.sorcicubecore.fight.FightData;
import fr.tangv.sorcicubecore.fight.FightStat;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
import fr.tangv.sorcicubecore.util.Cooldown;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.player.DataPlayer;
import net.minecraft.server.v1_9_R2.Packet;

public class Fight {
	
	//value static
	private final static double TOLERANCE = 4.6;
	private final static double TOLERANCE_ENTITY = 1.6;
	protected final static int MAX_HEALTH = 60;
	protected final static int START_HEALTH = 30;
	
	public final Config config;
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
		this.config = sorci.config();
		this.init = false;
		this.firstPlay = true;
		this.isStart = false;
		this.isDeleted = false;
		this.isEnd = false;
		this.spectators = new Vector<FightSpectator>();
		this.cooldown = new Cooldown(1_000);
		this.cooldownRound = new Cooldown(config.parameter.cooldownOneRound.value*1000L);
		this.cooldownEnd = new Cooldown(config.parameter.cooldownEnd.value*1000L);
		this.round = -config.parameter.cooldownBeforeFight.value-1;
		this.waitView = config.parameter.waitViewFight.value;
		this.arena = sorci.getManagerFight().pickArena();
		this.bossBar = Bukkit.createBossBar(
				config.gui.bossBar.nameArena.value.replace("{arena}", this.arena.getName()),
				BarColor.valueOf(config.gui.bossBar.colorArena.value),
				BarStyle.SOLID, new BarFlag[0]
			);
		//player1 start one
		if (Math.random() < 0.5) {
			this.player1 = createPlayerFight(preFight.getPlayer1(), preFight.getFeatures1(), preFight.getPlayer1DeckUse(), preFight.getDataPlayer1(), true);
			this.player2 = createPlayerFight(preFight.getPlayer2(), preFight.getFeatures2(), preFight.getPlayer2DeckUse(), preFight.getDataPlayer2(), false);
		} else {
			this.player2 = createPlayerFight(preFight.getPlayer1(), preFight.getFeatures1(), preFight.getPlayer1DeckUse(), preFight.getDataPlayer1(), false);
			this.player1 = createPlayerFight(preFight.getPlayer2(), preFight.getFeatures2(), preFight.getPlayer2DeckUse(), preFight.getDataPlayer2(), true);
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
		//--------
	}
	
	public void removeSpectator(FightSpectator spectator) {
		spectators.remove(spectator);
	}
	
	private PlayerFight createPlayerFight(Player player, PlayerFeatures features, int deck, DataPlayer dataPlayer, boolean first) throws Exception {
		return new PlayerFight(
				this, 
				player,
				new FightDeck(features.getDeck(deck)),
				first,
				dataPlayer
			);
	}
	
	public void update() throws IOException, ResponseRequestException, RequestException, DeckException {
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
								config.messages.belowStartGame.value
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
	
	public void nextRound() throws IOException, ResponseRequestException, RequestException, DeckException {
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
				config.messages.round.value
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
	
	public void end(PlayerFight losser) throws IOException, ResponseRequestException, RequestException, DeckException {
		if (player1 == losser)
			end(player1, player2);
		else
			end(player2, player1);
	}
	
	private void endReward(PlayerFight player, int money, int exp) throws IOException, ResponseRequestException, RequestException, DeckException {
		PlayerFeatures feature = sorci.getHandlerPlayers().getPlayer(player.getUUID(), player.getNamePlayer());
		player.sendMessage(
				config.messages.rewardEndGame.value
				.replace("{money}", Integer.toString(money))
				.replace("{experience}", Integer.toString(exp))
		);
		feature.addMoney(money);
		feature.addExperience(exp);
		while (!feature.isLevel((byte) config.level.maxLevel.value)) {
			int expNextLevel = config.level.getExperience(feature.getLevel()+1);
			if (feature.hasExperience(expNextLevel)) {
				feature.removeExperience(expNextLevel);
				feature.addLevel((byte) 1);
				int reward = config.level.getReward(feature.getLevel());
				feature.addMoney(reward);
				player.sendMessage(
						config.messages.changeLevel.value
						.replace("{level}", Byte.toString(feature.getLevel()))
						.replace("{money}", Integer.toString(reward))
				);
				player.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			} else
				break;
		}
		sorci.getHandlerPlayers().update(feature);
	}
	
	private void end(PlayerFight losser, PlayerFight winner) throws IOException, ResponseRequestException, RequestException, DeckException {
		fightData.setStat(FightStat.END);
		sorci.getHandlerFightData().updateFightData(fightData);
		this.cooldownEnd.start();
		bossBar.setColor(ValueFight.V.titleEndColor);
		bossBar.setTitle(ValueFight.V.titleEnd.replace("{time}", sorci.formatTime(cooldownEnd.getTimeRemaining())));
		bossBar.setProgress(cooldownEnd.getProgess());
		this.isEnd = true;
		if (losser.isDead() && winner.isDead()) {
			winner.sendEndTitle(config.messages.equality.value);
			losser.sendEndTitle(config.messages.equality.value);
			if (winner.isOnline())
				endReward(winner, config.level.moneyEquality.value, config.level.experienceEquality.value);
			if (losser.isOnline() && !losser.hasLossAFK())
				endReward(losser, config.level.moneyEquality.value, config.level.experienceEquality.value);
			//spectator
			forEachSpectator((FightSpectator spectator) -> {
				spectator.sendMessage(
						config.messages.spectatorEquality.value
						.replace("{playerw}", winner.getNamePlayer())
						.replace("{playerl}", losser.getNamePlayer())
				);
			});
		} else {
			winner.sendEndTitle(config.messages.winner.value);
			losser.sendEndTitle(config.messages.losser.value);
			if (winner.isOnline())
				endReward(winner, config.level.moneyWin.value, config.level.experienceWin.value);
			if (losser.isOnline() && !losser.hasLossAFK())
				endReward(losser, config.level.moneyLoss.value, config.level.experienceLoss.value);
			//spectator
			forEachSpectator((FightSpectator spectator) -> {
				spectator.sendMessage(
						config.messages.spectator.value
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