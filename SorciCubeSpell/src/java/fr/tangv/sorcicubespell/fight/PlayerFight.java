package fr.tangv.sorcicubespell.fight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardVisual;
import fr.tangv.sorcicubecore.fight.FightCible;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.util.ItemHead;

public class PlayerFight extends FightSpectator {

	private final Inventory invSwap;
    private PlayerFight enemie;
	private final FightDeck deck;
	private int mana;
	private int manaBoost;
	private int health;
	private int cardSelected;
	private FightEntity[] entity;
	private FightHero hero;
	private final Location[] entityLoc;
	private final Card[] cardHand;
	private FightEntity entityAttack;
	private FightHead firstSelection;
	private boolean alreadySwap;
	private byte roundAFK;
	private boolean isAFK;
	private boolean lossAFK;
	private boolean isDead;
	
	public PlayerFight(Fight fight, Player player, FightDeck deck, boolean first) {
		super(fight, player, first ? fight.getArena().getFirstBase() : fight.getArena().getSecondBase(), first);
		this.deck = deck;
		this.mana = 0;
		this.manaBoost = 0;
		this.health = Fight.START_HEALTH;
		this.cardSelected = -1;
		this.entityAttack = null;
		this.firstSelection = null;
		this.alreadySwap = false;
		this.roundAFK = 0;
		this.isAFK = true;
		this.lossAFK = false;
		this.isDead = false;
		//entity loc
		if (first) {
			this.entityLoc = fight.getArena().getFirstEntity();
		} else {
			this.entityLoc = fight.getArena().getSecondEntity();
		}
		//cards hand
		this.cardHand = new Card[6];
		this.pickCard(3);
		//historiquez
		this.invSwap = Bukkit.createInventory(player, 9, fight.getSorci().getGuiConfig().getString("gui_swap_fight.name"));
		addInventoryAutorized(invSwap);
		//init fightHead
		this.entity = new FightEntity[entityLoc.length];
		for (int i = 0; i < entityLoc.length; i++)
			this.entity[i] = new FightEntity(this, entityLoc[i]);
		this.hero = new FightHero(this);
	}
	
	public void checkPlayerIsDead() {
		if (isDead && !fight.isEnd())
			try {
				fight.end(this);
			} catch (IOException | ReponseRequestException | RequestException | DeckException e) {
				e.printStackTrace();
			}
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void newPlayer(Player player) {
		super.newPlayer(player);
		enemie.showPlayer(this);
		for (FightSpectator spectator : fight.getSpectators())
			spectator.showPlayer(this);
		initHotBar();
	}
	
	public void noAFK() {
		this.isAFK = false;
	}
	
	public boolean hasLossAFK() {
		return this.lossAFK;
	}

	public void addRoundAFK() throws IOException, ReponseRequestException, RequestException, DeckException {
		if (isAFK) {
			this.roundAFK++;
			if (roundAFK >= ValueFight.V.roundMaxAFK && !fight.isEnd()) {
				this.lossAFK = true;
				fight.sendMessage(fight.getSorci().getMessage().getString("message_afk_fight").replace("{player}", getNamePlayer()));
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
	
	public void openInvSwap() {
		//card hand
		for (int i = 0; i < getMaxCardHand(); i++)
			invSwap.setItem(i, getInvItem(i));
		//none
		for (int i = getMaxCardHand(); i < 9; i++)
			invSwap.setItem(i, ValueFight.V.itemNone);
		//open
		openInventory(invSwap);
	}
	
	public Inventory getInvSwap() {
		return invSwap;
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
	
	public boolean isFisrt() {
		return first;
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
		setLevel(mana);
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
		showPlayer(enemie);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (isDead)
			this.health = 0;
		else if (health <= 0) {
			this.health = 0;
			this.isDead = true;
		} else if (health > Fight.MAX_HEALTH) 
			this.health = Fight.MAX_HEALTH;
		else
			this.health = health;
		fight.updateViewLifes();
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
	
	public void sendEndTitle(String message) {
		if (isOnline()) {
			alert(message);
			sendMessageActionBar("");
		}
	}
	
	public void updateDisplayPlayer() {
		if (isOnline()) {
			String messageActionBar = "";
			if (canPlay()) {
				int cardSelected = getCardSelect();
				if (cardSelected != -1) {
					Card card = getCardHand(cardSelected);
					if (card != null) {
						messageActionBar = 
								CardVisual.renderManaCard(card)+"§r§f \u25b6 "+card.renderName()+"§r§f \u25c0 "+
								(card.getType() == CardType.ENTITY ? CardVisual.renderStatCard(card) : CardVisual.renderManaCard(card));
					}
				}
				setExp(1F);
			} else {
				setExp(0F);
			}
			sendMessageActionBar(messageActionBar);
			setLevel(getMana());
		}
	}
	
	public void initHotBar() {
		if (isOnline()) {
			boolean play = canPlay();
			setInvItem(FightSlot.NONE_1.getSlotInv(), ValueFight.V.itemNone);
			setInvItem(FightSlot.STICK_VIEW.getSlotInv(), ValueFight.V.itemStickView);
			ItemStack item = ValueFight.V.itemNone;
			if (play)
				item = ValueFight.V.itemNextRound;
			setInvItem(FightSlot.FINISH_ROUND.getSlotInv(), item);
			//card hand
			setInvItem(FightSlot.CARD_1.getSlotInv(), itemNull(fight.renderCard(getCardHand(0))));
			setInvItem(FightSlot.CARD_2.getSlotInv(), itemNull(fight.renderCard(getCardHand(1))));
			setInvItem(FightSlot.CARD_3.getSlotInv(), itemNull(fight.renderCard(getCardHand(2))));
			setInvItem(FightSlot.CARD_4.getSlotInv(), itemNull(fight.renderCard(getCardHand(3))));
			setInvItem(FightSlot.CARD_5.getSlotInv(), itemNull(fight.renderCard(getCardHand(4))));
			setInvItem(FightSlot.CARD_6.getSlotInv(), itemNull(fight.renderCard(getCardHand(5))));
			//inv in
			setInvItem(FightSlot.BUY_CARD.getSlotInv(), ValueFight.V.itemBuy);
			setInvItem(FightSlot.SWAP_CARD.getSlotInv(), alreadySwap ? null : ValueFight.V.itemSwap);
			updateInventory();
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
					showHeadForAttack(CardCible.ONE_ENTITY_ALLY, card.getCibleFaction(), ItemHead.SELECTABLE_ENTITY_AAE);
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
		for (FightCible cible : FightCibles.listForCardCible(cibleC)) {
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
	
}