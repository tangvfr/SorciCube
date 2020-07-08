package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardRender;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class PlayerFight {

	private Inventory invHistoric;
	private Fight fight;
    private PlayerFight enemie;
	private Player player;
	private FightDeck deck;
	private boolean first;
	private int mana;
	private FightEntity[] entity;
	private Location locBase;
	private Card[] cardHand;
	private ItemStack itemNextRound;
	
	public PlayerFight(Fight fight, Player player, FightDeck deck, boolean first) {
		this.fight = fight;
		this.player = player;
		this.deck = deck;
		this.setMana(0);
		this.first = first;
		this.locBase = this.isFisrt() ? fight.getArena().getFirstBase() : fight.getArena().getSecondBase();
		//item
		this.itemNextRound = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, "§6Next Round", null, false);
		//entity
		Location[] locs = this.isFisrt() ? fight.getArena().getFirstEntity() : fight.getArena().getSecondEntity();
		this.entity = new FightEntity[locs.length];
		for (int i = 0; i < locs.length; i++)
			this.entity[i] = new FightEntity(fight, locs[i]);
		//cards hand
		this.cardHand = new Card[6];
		for (int i = 0; i < 4; i++)
			cardHand[i] = getDeck().pickCard();
		//historique
		this.invHistoric = Bukkit.createInventory(player, 9, fight.getSorci().gertGuiConfig().getString("gui_historic.name"));
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
	
	public void openInvHistoric() {
		player.openInventory(invHistoric);
	}
	
	public Inventory getInvHistoric() {
		return invHistoric;
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
	
	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public PlayerFight getEnemie() {
		return enemie;
	}

	public void setEnemie(PlayerFight enemie) {
		this.enemie = enemie;
		player.showPlayer(enemie.getPlayer());
	}
	
	//function
	
	public void initHotBar() {
		boolean play = canPlay();
		player.getInventory().setItem(FightSlot.NONE_1.getSlotInv(), null);
		player.getInventory().setItem(FightSlot.NONE_2.getSlotInv(), null);
		ItemStack item = null;
		if (play)
			item = itemNextRound;
		player.getInventory().setItem(FightSlot.FINISH_ROUND.getSlotInv(), item);
		//card hand
		player.getInventory().setItem(FightSlot.CARD_1.getSlotInv(), CardRender.cardToItem(getCardHand(0), fight.getSorci()));
		player.getInventory().setItem(FightSlot.CARD_2.getSlotInv(), CardRender.cardToItem(getCardHand(1), fight.getSorci()));
		player.getInventory().setItem(FightSlot.CARD_3.getSlotInv(), CardRender.cardToItem(getCardHand(2), fight.getSorci()));
		player.getInventory().setItem(FightSlot.CARD_4.getSlotInv(), CardRender.cardToItem(getCardHand(3), fight.getSorci()));
		player.getInventory().setItem(FightSlot.CARD_5.getSlotInv(), CardRender.cardToItem(getCardHand(4), fight.getSorci()));
		player.getInventory().setItem(FightSlot.CARD_6.getSlotInv(), CardRender.cardToItem(getCardHand(5), fight.getSorci()));
		player.updateInventory();
	}
	
	public boolean canPlay() {
		return fight.gameIsStart() && fight.getFirstPlay() == first;
	}
	
	public boolean isPlayer(Player player) {
		return this.player.equals(player);
	}
	
	public boolean teleportToBase() {
		return player.teleport(this.locBase, TeleportCause.PLUGIN);
	}
	
}
