package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.util.ItemBuild;
import net.minecraft.server.v1_9_R2.IScoreboardCriteria;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_9_R2.Scoreboard;
import net.minecraft.server.v1_9_R2.ScoreboardObjective;
import net.minecraft.server.v1_9_R2.ScoreboardScore;

public class PlayerFight {

	private final static int MAX_HEALTH = 60;
	private final static int START_HEALTH = 30;
	private Inventory invHistoric;
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
	private Location locBase;
	private Card[] cardHand;
	private ItemStack itemNextRound;
	
	public PlayerFight(Fight fight, Player player, FightDeck deck, boolean first) {
		this.fight = fight;
		this.player = player;
		this.deck = deck;
		this.mana = 0;
		this.manaBoost = 0;
		this.health = START_HEALTH;
		this.cardSelected = -1;
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
		this.pickCard(3);
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

	public int getManaBoost() {
		return manaBoost;
	}

	public void setManaBoost(int manaBoost) {
		this.manaBoost = manaBoost;
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
		if (health < 0)
			this.health = 0;
		else if (health > MAX_HEALTH) 
			this.health = MAX_HEALTH;
		else
			this.health = health;
		updateViewLifes();
		getEnemie().updateViewLifes();
	}
	
	public int getCardSelect() {
		return cardSelected;
	}
	
	public void setCardSelect(int index) {
		if (index < 0 || index > cardHand.length-1 || cardHand[index] == null)
			this.cardSelected = -1;
		else
			this.cardSelected = index;
	}
	
	//function

	public void sendPacket(Packet<?> packet) {
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
	
	public void initHotBar() {
		boolean play = canPlay();
		player.getInventory().setItem(FightSlot.NONE_1.getSlotInv(), null);
		player.getInventory().setItem(FightSlot.NONE_2.getSlotInv(), null);
		ItemStack item = null;
		if (play)
			item = itemNextRound;
		player.getInventory().setItem(FightSlot.FINISH_ROUND.getSlotInv(), item);
		//card hand
		player.getInventory().setItem(FightSlot.CARD_1.getSlotInv(), fight.renderCard(getCardHand(0)));
		player.getInventory().setItem(FightSlot.CARD_2.getSlotInv(), fight.renderCard(getCardHand(1)));
		player.getInventory().setItem(FightSlot.CARD_3.getSlotInv(), fight.renderCard(getCardHand(2)));
		player.getInventory().setItem(FightSlot.CARD_4.getSlotInv(), fight.renderCard(getCardHand(3)));
		player.getInventory().setItem(FightSlot.CARD_5.getSlotInv(), fight.renderCard(getCardHand(4)));
		player.getInventory().setItem(FightSlot.CARD_6.getSlotInv(), fight.renderCard(getCardHand(5)));
		player.updateInventory();
	}
	
	public void showSelectCard() {
		if (cardSelected != -1) {
			//init the cible possible
			player.closeInventory();
		}
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
