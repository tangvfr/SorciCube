package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.util.ItemHead;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;

public class PlayerFight {

	private final Inventory invSwap;
	private final Inventory invHistoric;
	private final Inventory invViewEntity;
	private final Fight fight;
    private volatile PlayerFight enemie;
	private final Player player;
	private final FightDeck deck;
	private final boolean first;
	private volatile int mana;
	private volatile int manaBoost;
	private volatile int health;
	private volatile int cardSelected;
	private volatile FightEntity[] entity;
	private volatile FightHero hero;
	private final Location locBase;
	private final Location[] entityLoc;
	private final Card[] cardHand;
	private volatile FightEntity entityAttack;
	private volatile FightHead firstSelection;
	private volatile boolean alreadySwap;
	private volatile byte roundAFK;
	private volatile boolean isAFK;
	private volatile boolean lossAFK;
	
	//scoreboard
	private volatile String[] lastScoreMy;
	private volatile String[] lastScoreEnemie;
	private volatile Scoreboard sc;
	private volatile ScoreboardObjective scob;
	
	public PlayerFight(Fight fight, Player player, FightDeck deck, boolean first) {
		this.fight = fight;
		this.player = player;
		this.deck = deck;
		this.mana = 0;
		this.manaBoost = 0;
		this.health = Fight.start_health;
		this.cardSelected = -1;
		this.first = first;
		this.entityAttack = null;
		this.firstSelection = null;
		this.alreadySwap = false;
		this.roundAFK = 0;
		this.isAFK = true;
		this.lossAFK = false;
		//entity loc
		if (first) {
			this.locBase = fight.getArena().getFirstBase();
			this.entityLoc = fight.getArena().getFirstEntity();
		} else {
			this.locBase = fight.getArena().getSecondBase();
			this.entityLoc = fight.getArena().getSecondEntity();
		}
		//cards hand
		this.cardHand = new Card[6];
		this.pickCard(3);
		//historique
		this.invHistoric = Bukkit.createInventory(player, 9, fight.getSorci().gertGuiConfig().getString("gui_historic.name"));
		this.invSwap = Bukkit.createInventory(player, 9, fight.getSorci().gertGuiConfig().getString("gui_swap_fight.name"));
		this.invViewEntity = Bukkit.createInventory(player, InventoryType.DISPENSER, fight.getSorci().gertGuiConfig().getString("gui_view_entity.name"));
	}
	
	public void sendMessage(String message) {
		sendPacket(new PacketPlayOutChat(Fight.toIChatBaseComposent(message), (byte) 0));
	}
	
	public void noAFK() {
		this.isAFK = false;
	}
	
	public boolean hasLossAFK() {
		return this.lossAFK;
	}

	public void addRoundAFK() {
		if (isAFK) {
			this.roundAFK++;
			if (roundAFK >= ValueFight.V.roundMaxAFK) {
				this.lossAFK = true;
				fight.sendPacket(new PacketPlayOutChat(Fight.toIChatBaseComposent(
						fight.getSorci().getMessage().getString("message_afk_fight").replace("{player}", player.getName())
				), (byte) 0));
				fight.end(this);
			}
		} else {
			this.roundAFK = 0;
			this.isAFK = true;
		}
	}

	public boolean hasAlreadySwap() {
		return alreadySwap;
	}

	public void setAlreadySwap(boolean alreadySwap) {
		this.alreadySwap = alreadySwap;
	}

	public void nextRoundFightEntity() {
		for (FightEntity entity : entity)
			if(!entity.isDead())
				if (entity.getCard().nextRound())
					entity.updateStat();
	}
	
	public void resetEntityAttackPossible() {
		for (FightEntity entity : entity)
			entity.setAttackPossible(!entity.isDead() && !(entity.getCard().isImmobilization() || entity.getCard().isStunned()));
	}
	
	public void showEntityAttackPossible() {
		hideAllHead();
		for (FightEntity entity : entity)
			if (!entity.isDead() && entity.attackIsPossible()) 
				entity.showHead(ItemHead.SELECTABLE_ENTITY_ATTACK);
	}
	
	public boolean hasFirstSelection() {
		return firstSelection != null;
	}
	
	public FightHead getFirstSelection() {
		return this.firstSelection;
	}
	

	public void setFirstSelection(FightHead firstSelection) {
		this.firstSelection = firstSelection;
	}
	
	public boolean hasEntityAttack() {
		return entityAttack != null;
	}
	
	public FightEntity getEntityAttack() {
		return entityAttack;
	}
	
	public void setEntityAttack(FightEntity entityAttack) {
		this.entityAttack = entityAttack; 
	}
	
	public void initFightHead() {
		this.entity = new FightEntity[entityLoc.length];
		for (int i = 0; i < entityLoc.length; i++)
			this.entity[i] = new FightEntity(this, entityLoc[i]);
		this.hero = new FightHero(this);
	}
	
	//param number is number card pick
	//return number card is pick
	public int pickCard(int number) {
		int numberPick = 0;
		for (int i = 0; i < getMaxCardHand(); i++) {
			if (numberPick >= number)
				break;
			if (getCardHand(i) == null) {
				setCardHand(i, getDeck().pickCard());
				numberPick += 1;
			}
		}
		return numberPick;
	}
	
	public int giveCard(Card card, int number) {
		int numberPick = 0;
		for (int i = 0; i < getMaxCardHand(); i++) {
			if (numberPick >= number)
				break;
			if (getCardHand(i) == null) {
				setCardHand(i, card.clone());
				numberPick += 1;
			}
		}
		return numberPick;
	}
	
	private ItemStack itemNull(ItemStack item) {
		if (item == null)
			return ValueFight.V.itemNull;
		else
			return item;
	}
	
	public void openInvViewEntity(ItemStack item) {
		invViewEntity.setItem(4, item);
		player.openInventory(invViewEntity);
	}
	
	public Inventory getInvViewEntity() {
		return invViewEntity;
	}
	
	public void addHistoric(Card card, PlayerFight owner) {
		ItemStack item = CardRender.cardToItem(card, fight.getSorci(), (this == owner) ? 2 : 1, false);
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
	
	public void openInvSwap() {
		//card hand
		for (int i = 0; i < getMaxCardHand(); i++)
			invSwap.setItem(i, player.getPlayer().getInventory().getItem(i));
		//none
		for (int i = getMaxCardHand(); i < 9; i++)
			invSwap.setItem(i, ValueFight.V.itemNone);
		//open
		player.openInventory(invSwap);
	}
	
	public Inventory getInvSwap() {
		return invSwap;
	}
	
	public void closeInventory() {
		if (player.isOnline())
			player.closeInventory();
	}
	
	public Location getLocBase() {
		return this.locBase;
	}
	
	public int getMaxCardHand() {
		return cardHand.length;
	}
	
	public void setCardHand(int index, Card card) {
		cardHand[index] = card;
	}
	
	public Card getCardHand(int index) {
		return cardHand[index];
	}
	
	public Fight getFight() {
		return fight;
	}
	
	public boolean isFisrt() {
		return first;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public FightDeck getDeck() {
		return deck;
	}
	
	public FightEntity getEntity(int index) {
		return entity[index];
	}
	
	public int getMaxEntity() {
		return entity.length;
	}
	
	public FightHero getHero() {
		return hero;
	}
	
	public void hideAllHead() {
		this.getEntity(0).hideHead();
		this.getEntity(1).hideHead();
		this.getEntity(2).hideHead();
		this.getEntity(3).hideHead();
		this.getEntity(4).hideHead();
		this.getHero().hideHead();
		enemie.getEntity(0).hideHead();
		enemie.getEntity(1).hideHead();
		enemie.getEntity(2).hideHead();
		enemie.getEntity(3).hideHead();
		enemie.getEntity(4).hideHead();
		enemie.getHero().hideHead();
	}
	
	public Vector<FightHead> getForCibles(Collection<FightCible> cibles) {
		Vector<FightHead> heads = new Vector<FightHead>();
		for (FightCible cible : cibles)
			heads.add(getForCible(cible));
		return heads;
	}
	
	public FightHead getForCible(FightCible cible) {
		switch (cible) {
			case ENTITY_1_ALLY:
				return this.getEntity(0);
			
			case ENTITY_2_ALLY:
				return this.getEntity(1);
				
			case ENTITY_3_ALLY:
				return this.getEntity(2);
				
			case ENTITY_4_ALLY:
				return this.getEntity(3);
				
			case ENTITY_5_ALLY:
				return this.getEntity(4);
				
			case HERO_ALLY:
				return this.getHero();
				
			case ENTITY_1_ENEMIE:
				return enemie.getEntity(0);
			
			case ENTITY_2_ENEMIE:
				return enemie.getEntity(1);
				
			case ENTITY_3_ENEMIE:
				return enemie.getEntity(2);
				
			case ENTITY_4_ENEMIE:
				return enemie.getEntity(3);
				
			case ENTITY_5_ENEMIE:
				return enemie.getEntity(4);
				
			case HERO_ENEMIE:
				return enemie.getHero();
				
			default:
				return null;//not possible
		}
	}
	
	public int getMana() {
		return mana;
	}

	public boolean hasMana(int manaHave) {
		return mana >= manaHave;
	}
	
	public void setMana(int mana) {
		if (mana < 0)
			mana = 0;
		this.mana = mana;
		player.getPlayer().setLevel(mana);
		this.hero.updateStat();
	}
	
	public void addMana(int mana) {
		setMana(getMana()+mana);
	}
	
	public void removeMana(int mana) {
		setMana(getMana()-mana);
	}

	public int getManaBoost() {
		return manaBoost;
	}

	public void setManaBoost(int manaBoost) {
		this.manaBoost = manaBoost;
		this.hero.updateStat();
	}
	
	public void addManaBoost(int manaBoost) {
		setManaBoost(getManaBoost()+manaBoost);
	}
	
	public void removeManaBoost(int manaBoost) {
		setManaBoost(getManaBoost()-manaBoost);
	}
	
	public PlayerFight getEnemie() {
		return enemie;
	}

	public void setEnemie(PlayerFight enemie) {
		this.enemie = enemie;
		Bukkit.getScheduler().runTask(fight.getSorci(), new Runnable() {
			@Override
			public void run() {
				player.showPlayer(enemie.getPlayer());
			}
		});
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (health <= 0) {
			this.health = 0;
			fight.end(this);
		} else if (health > Fight.max_health) 
			this.health = Fight.max_health;
		else
			this.health = health;
		updateViewLifes();
		getEnemie().updateViewLifes();
		this.hero.updateStat();
	}
	
	public int getCardSelect() {
		return cardSelected;
	}
	
	public void setCardSelect(int index) {
		if (index < 0 || index > cardHand.length-1 || cardHand[index] == null)
			this.cardSelected = -1;
		else
			this.cardSelected = index;
		showSelectCard();
	}
	
	//function

	public void sendPacket(Packet<?> packet) {
		if (player.isOnline())
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void alert(String message) {
		if (player.isOnline()) {
			player.sendMessage(message);
			sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE,
					Fight.toIChatBaseComposent(""),
					0, 6, 0));
			sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
					Fight.toIChatBaseComposent(message),
					0, 6, 0));
		}
	}
	
	public void sendEndTitle(String message) {
		if (player.isOnline()) {
			alert(message);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent(""));
		}
	}
	
	public void returnLobby() {
		if (player.isOnline())
			fight.getSorci().sendPlayerToServer(player, fight.getSorci().getNameServerLobby());
	}
	
	public void updateDisplayPlayer() {
		if (player.isOnline()) {
			String messageActionBar = "";
			if (canPlay()) {
				int cardSelected = getCardSelect();
				if (cardSelected != -1) {
					Card card = getCardHand(cardSelected);
					messageActionBar = 
							CardRender.renderManaCard(card)+"§r§f \u25b6 "+card.renderName()+"§r§f \u25c0 "+
							(card.getType() == CardType.ENTITY ? CardRender.renderStatCard(card) : CardRender.renderManaCard(card));
				}
				player.setExp(1F);
			} else {
				player.setExp(0F);
			}
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent(messageActionBar));
			player.setLevel(getMana());
		}
	}
	
	private void sendScore(String name, String lastName, int scoreNumber) {
		if (lastName != null)
			sendPacket(new PacketPlayOutScoreboardScore(lastName)/*remove*/);
		ScoreboardScore score = new ScoreboardScore(this.sc, this.scob, name/*text display*/);
		score.setScore(scoreNumber);
		sendPacket(new PacketPlayOutScoreboardScore(score)/*change*/);
	}
	
	public void createScoreboard() {
		this.sc = new Scoreboard();
		this.scob = new ScoreboardObjective(sc, 
				fight.getSorci().gertGuiConfig().getString("scoreboard.name")/*displayName*/,
				IScoreboardCriteria.b/*dummy*/
			);
		sendPacket(new PacketPlayOutScoreboardObjective(scob, 0/*0 create, 1 remmove, 2 update*/));
		sendPacket(new PacketPlayOutScoreboardDisplayObjective(1/*0 LIST, 1 SIDEBAR, 2 BELOW_NAME*/, scob));
		//init score
		this.lastScoreMy = healthToString(false);
		this.lastScoreEnemie = getEnemie().healthToString(true);
		sendScore(" ", null, -1);
		sendScore("§7"+getPlayer().getName()+"§8:", null, -2);
		sendScore(this.lastScoreMy[0], null, -3);
		sendScore(this.lastScoreMy[1], null, -4);
		sendScore(this.lastScoreMy[2], null, -5);
		sendScore("   ", null, -6);
		sendScore("    ", null, -7);
		sendScore("     ", null, -8);
		sendScore("      ", null, -9);
		sendScore("       ", null, -10);
		sendScore("§7"+getEnemie().getPlayer().getName()+"§8:", null, -11);
		sendScore(this.lastScoreEnemie[0], null, -12);
		sendScore(this.lastScoreEnemie[1], null, -13);
		sendScore(this.lastScoreEnemie[2], null, -14);
		sendScore("         ", null, -15);
		//update objective
		sendPacket(new PacketPlayOutScoreboardObjective(scob, 2/*0 create, 1 remmove, 2 update*/));
	}
	
	public void updateViewLifes() {
		String[] scoreMy = healthToString(false);
		String[] scoreEnemie = getEnemie().healthToString(true);
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
	
	private String[] healthToString(boolean enemie) {
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
	
	public boolean hasStickView() {
		return player.getInventory().getItemInMainHand().isSimilar(ValueFight.V.itemStickView);
	}
	
	public void initHotBar() {
		if (player.isOnline()) {
			boolean play = canPlay();
			player.getInventory().setItem(FightSlot.NONE_1.getSlotInv(), ValueFight.V.itemNone);
			player.getInventory().setItem(FightSlot.STICK_VIEW.getSlotInv(), ValueFight.V.itemStickView);
			ItemStack item = ValueFight.V.itemNone;
			if (play)
				item = ValueFight.V.itemNextRound;
			player.getInventory().setItem(FightSlot.FINISH_ROUND.getSlotInv(), item);
			//card hand
			player.getInventory().setItem(FightSlot.CARD_1.getSlotInv(), itemNull(fight.renderCard(getCardHand(0))));
			player.getInventory().setItem(FightSlot.CARD_2.getSlotInv(), itemNull(fight.renderCard(getCardHand(1))));
			player.getInventory().setItem(FightSlot.CARD_3.getSlotInv(), itemNull(fight.renderCard(getCardHand(2))));
			player.getInventory().setItem(FightSlot.CARD_4.getSlotInv(), itemNull(fight.renderCard(getCardHand(3))));
			player.getInventory().setItem(FightSlot.CARD_5.getSlotInv(), itemNull(fight.renderCard(getCardHand(4))));
			player.getInventory().setItem(FightSlot.CARD_6.getSlotInv(), itemNull(fight.renderCard(getCardHand(5))));
			//inv in
			player.getInventory().setItem(FightSlot.BUY_CARD.getSlotInv(), ValueFight.V.itemBuy);
			player.getInventory().setItem(FightSlot.SWAP_CARD.getSlotInv(), alreadySwap ? null : ValueFight.V.itemSwap);
			player.updateInventory();
		}
	}
	
	private void showSelectCard() {
		hideAllHead();
		if (cardSelected != -1) {
			closeInventory();
			Card card = cardHand[cardSelected];
			if (card.getType() == CardType.ENTITY) {
				initHeadForEntityPose(card);
			} else {
				if (card.getCible() == CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE)
					showHeadForAttack(CardCible.ONE_ENTITY_ALLY, card.getFaction(), ItemHead.SELECTABLE_ENTITY_AAE);
				else if (card.getCible().hasChoose())
					showHeadForAttack(card, ItemHead.SELECTABLE_SPELL);
				else
					showHeadForAttackAll(card, ItemHead.SELECTABLE_ALL_SPELL);
			}
		}
	}
	
	public void initHeadForEntityPose(Card card) {
		hideAllHead();
		for (FightEntity entity : entity)
			if (entity.isDead())
				entity.showHead(ItemHead.SELECTABLE_POSE);
	}
		
	public boolean showHeadForAttack(Card card, ItemStack headItem) {
		return showHeadForAttack(card.getCible(), card.getCibleFaction(), headItem);
	}
	
	public boolean showHeadForAttack(CardCible cibleC, CardFaction cibleFaction, ItemStack headItem) {
		hideAllHead();
		return executeFightHeadIsGoodCible(cibleC, cibleFaction, new ResultFightHead() {
			@Override
			public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
				boolean possible = false;
				if (incitement) {
					for (FightHead head : fightHeads)
						if (head.hasIncitement() || head.getOwner() == PlayerFight.this) {
							possible = true;
							head.showHead(headItem);
						}
				} else {
					for (FightHead head : fightHeads) {
						possible = true;
						head.showHead(headItem);
					}
				}
				return possible;
			}
		});
	}
	
	public boolean showHeadForAttackAll(Card card, ItemStack headItem) {
		return showHeadForAttackAll(card.getCible(), card.getCibleFaction(), headItem);
	}
	
	public boolean showHeadForAttackAll(CardCible cibleC, CardFaction cibleFaction, ItemStack headItem) {
		hideAllHead();
		return executeFightHeadIsGoodCible(cibleC, cibleFaction, new ResultFightHead() {
			@Override
			public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
				boolean possible = false;
				for (FightHead head : fightHeads) {
					possible = true;
					head.showHead(headItem);
				}
				return possible;
			}
		});
	}
	
	public boolean testHeadValidForAttack(Card card, FightHead head) {
		return testHeadValidForAttack(card.getCible(), card.getCibleFaction(), head);
	}
	
	public boolean testHeadValidForAttack(CardCible cibleC, CardFaction cibleFaction, FightHead head) {
		return executeFightHeadIsGoodCible(cibleC, cibleFaction, new ResultFightHead() {
			@Override
			public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
				return fightHeads.contains(head) && (incitement ? (head.hasIncitement() || head.getOwner() == PlayerFight.this) : true);
			}
		});
	}
	
	public boolean executeFightHeadIsGoodCible(Card card, ResultFightHead resultFightHead) {
		return executeFightHeadIsGoodCible(card.getCible(), card.getCibleFaction(), resultFightHead);
	}
	
	public boolean executeFightHeadIsGoodCible(CardCible cibleC, CardFaction cibleFaction, ResultFightHead resultFightHead) {
		ArrayList<FightHead> fightHeads = new ArrayList<FightHead>();
		boolean incitement = false;
		for (FightCible cible : FightCible.listForCardCible(cibleC)) {
			FightHead head = getForCible(cible);
			if (head.isSelectable() && head.isFaction(cibleFaction)) {
				if (head.hasIncitement() && head.getOwner() != PlayerFight.this)
					incitement = true;
				fightHeads.add(head);
			}
		}
		return resultFightHead.resultFightHead(fightHeads, incitement);
	}
	
	public static interface ResultFightHead {
		public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement);
	}
	
	public boolean canPlay() {
		return fight.isStart() && !fight.isEnd() && fight.getFirstPlay() == first;
	}
	
	public boolean isPlayer(Player player) {
		return this.player.equals(player);
	}
	
	public boolean teleportToBase() {
		return player.teleport(this.locBase, TeleportCause.PLUGIN);
	}
	
}