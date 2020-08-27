package fr.tangv.sorcicubespell.gui;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardComparator;
import fr.tangv.sorcicubespell.fight.FightType;
import fr.tangv.sorcicubespell.player.PlayerFeature;

public class PlayerGui {

	private Player player;
	private Card card;
	private Inventory invOfGui;
	private AbstractGui gui;
	private CardComparator cardComparator;
	private int pageViewGui;
	private int deckEdit;
	private int deckCardEdit;
	private PlayerFeature playerFeature;
	private AbstractGui previousGui;
	private FightType fightType;
	private Player inviteDuel;
	
	public PlayerGui(Player player) {
		this.player = player;
		this.card = null;
		this.gui = null;
		this.inviteDuel = null;
		this.cardComparator = CardComparator.BY_ID;
		this.setPageViewGui(0);
		this.invOfGui = null;
		this.setDeckEdit(1);
		this.setDeckCardEdit(0);
		this.setPlayerFeature(null);
		this.setPreviousGui(null);
	}
	
	public UUID getUUID() {
		return player.getUniqueId();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Card getCard() {
		return card;
	}
	
	public void setCart(Card card) {
		this.card = card;
	}
	
	public AbstractGui getGui() {
		return gui;
	}
	
	public void setGui(AbstractGui gui) {
		this.gui = gui;
		this.invOfGui = null;
	}

	public int getPageViewGui() {
		return pageViewGui;
	}

	public void setPageViewGui(int pageViewGui) {
		this.pageViewGui = pageViewGui;
	}

	public CardComparator getCardComparator() {
		return cardComparator;
	}

	public void setCardComparator(CardComparator cardComparator) {
		this.cardComparator = cardComparator;
	}

	public Inventory getInvOfGui() {
		return invOfGui;
	}
	
	public void setInvOfGui(Inventory invOfGui) {
		this.invOfGui = invOfGui;
	}

	public int getDeckEdit() {
		return deckEdit;
	}

	public void setDeckEdit(int deckEdit) {
		this.deckEdit = deckEdit;
	}

	public int getDeckCardEdit() {
		return deckCardEdit;
	}

	public void setDeckCardEdit(int deckCardEdit) {
		this.deckCardEdit = deckCardEdit;
	}

	public PlayerFeature getPlayerFeature() {
		return playerFeature;
	}

	public void setPlayerFeature(PlayerFeature playerFeature) {
		this.playerFeature = playerFeature;
	}

	public AbstractGui getPreviousGui() {
		return previousGui;
	}

	public void setPreviousGui(AbstractGui previousGui) {
		this.previousGui = previousGui;
	}

	public FightType getFightType() {
		return fightType;
	}

	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public Player getInviteDuel() {
		return inviteDuel;
	}

	public void setInviteDuel(Player inviteDuel) {
		this.inviteDuel = inviteDuel;
	}
	
}
