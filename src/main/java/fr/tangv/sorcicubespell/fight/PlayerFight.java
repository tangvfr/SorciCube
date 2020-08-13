package fr.tangv.sorcicubespell.fight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.ItemHead;
import net.minecraft.server.v1_9_R2.IScoreboardCriteria;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_9_R2.Scoreboard;
import net.minecraft.server.v1_9_R2.ScoreboardObjective;
import net.minecraft.server.v1_9_R2.ScoreboardScore;

public class PlayerFight {

	private Inventory invHistoric;
	private Inventory invViewEntity;
	private Fight fight;
    private PlayerFight enemie;
	private Player player;
	private FightDeck deck;
	private boolean first;
	private int mana;
	private int manaBoost;
	private int health;
	private int cardSelected;
	private FightEntity[] entity;
	private FightHero hero;
	private Location locBase;
	private Location[] entityLoc;
	private Card[] cardHand;
	private ItemStack itemNextRound;
	private ItemStack itemNone;
	private ItemStack itemNull;
	private ItemStack itemStickView;
	private FightEntity entityAttack;
	
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
		//item
		this.itemNone = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, fight.getSorci().gertGuiConfig().getString("gui_player.none"), null, false);
		this.itemNull = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 8, fight.getSorci().gertGuiConfig().getString("gui_player.null"), null, false);
		this.itemNextRound = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, fight.getSorci().gertGuiConfig().getString("gui_player.next"), null, false);
		this.itemStickView = ItemBuild.buildItem(Material.BLAZE_ROD, 1, (short) 0, (byte) 0, fight.getSorci().gertGuiConfig().getString("gui_player.stick_view"), null, false);
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
		this.invViewEntity = Bukkit.createInventory(player, InventoryType.DROPPER, fight.getSorci().gertGuiConfig().getString("gui_view_entity.name"));
	}
	
	public void nextRoundFightEntity() {
		for (FightEntity entity : entity)
			if(!entity.isDead())
				entity.getCard().nextRound();
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
			return itemNull;
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
		player.showPlayer(enemie.getPlayer());
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (health <= 0) {
			this.health = 0;
			fight.end(player);
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
		sendPacket(player, packet);
	}
	
	public static void sendPacket(Player player, Packet<?> packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void sendScore(String name, String lastName, int scoreNumber) {
		if (lastName != null)
			sendPacket(new PacketPlayOutScoreboardScore(lastName)/*remove*/);
		ScoreboardScore score = new ScoreboardScore(this.sc, this.scob, name/*text display*/);
		score.setScore(scoreNumber);
		sendPacket(new PacketPlayOutScoreboardScore(score)/*change*/);
	}
	
	private String[] lastScoreMy;
	private String[] lastScoreEnemie;
	private Scoreboard sc;
	private ScoreboardObjective scob;
	
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
		return player.getInventory().getItemInMainHand().isSimilar(itemStickView);
	}
	
	public void initHotBar() {
		boolean play = canPlay();
		player.getInventory().setItem(FightSlot.NONE_1.getSlotInv(), itemNone);
		player.getInventory().setItem(FightSlot.STICK_VIEW.getSlotInv(), itemStickView);
		ItemStack item = itemNone;
		if (play)
			item = itemNextRound;
		player.getInventory().setItem(FightSlot.FINISH_ROUND.getSlotInv(), item);
		//card hand
		player.getInventory().setItem(FightSlot.CARD_1.getSlotInv(), itemNull(fight.renderCard(getCardHand(0))));
		player.getInventory().setItem(FightSlot.CARD_2.getSlotInv(), itemNull(fight.renderCard(getCardHand(1))));
		player.getInventory().setItem(FightSlot.CARD_3.getSlotInv(), itemNull(fight.renderCard(getCardHand(2))));
		player.getInventory().setItem(FightSlot.CARD_4.getSlotInv(), itemNull(fight.renderCard(getCardHand(3))));
		player.getInventory().setItem(FightSlot.CARD_5.getSlotInv(), itemNull(fight.renderCard(getCardHand(4))));
		player.getInventory().setItem(FightSlot.CARD_6.getSlotInv(), itemNull(fight.renderCard(getCardHand(5))));
		player.updateInventory();
	}
	
	private void showSelectCard() {
		hideAllHead();
		if (cardSelected != -1) {
			player.closeInventory();
			Card card = cardHand[cardSelected];
			if (card.getType() == CardType.ENTITY) {
				initHeadForEntityPose(card);
			} else {
				if (card.getCible().hasChoose())
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
		hideAllHead();
		return executeFightHeadIsGoodCible(card, new ResultFightHead() {
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
		hideAllHead();
		return executeFightHeadIsGoodCible(card, new ResultFightHead() {
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
		return executeFightHeadIsGoodCible(card, new ResultFightHead() {
			@Override
			public boolean resultFightHead(ArrayList<FightHead> fightHeads, boolean incitement) {
				return fightHeads.contains(head) && (incitement ? (head.hasIncitement() || head.getOwner() == PlayerFight.this) : true);
			}
		});
	}
	
	public boolean executeFightHeadIsGoodCible(Card card, ResultFightHead resultFightHead) {
		ArrayList<FightHead> fightHeads = new ArrayList<FightHead>();
		boolean incitement = false;
		for (FightCible cible : FightCible.listForCardCible(card.getCible())) {
			FightHead head = getForCible(cible);
			if (head.isSelectable() && head.isFaction(card.getCibleFaction())) {
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