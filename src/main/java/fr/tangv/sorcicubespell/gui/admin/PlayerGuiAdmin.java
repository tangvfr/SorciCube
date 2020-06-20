package fr.tangv.sorcicubespell.gui.admin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.cards.Card;
import fr.tangv.sorcicubespell.cards.CardComparator;

public class PlayerGuiAdmin {

	private Player player;
	private Card card;
	private Inventory invOfGui;
	private AbstractGuiAdmin gui;
	private CardComparator cardComparator;
	private int pageEditGui;
	
	public PlayerGuiAdmin(Player player) {
		this.player = player;
		this.card = null;
		this.gui = null;
		this.cardComparator = CardComparator.BY_ID;
		this.pageEditGui = 0;
		this.invOfGui = null;
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
	
	public AbstractGuiAdmin getGui() {
		return gui;
	}
	
	public void setGuiAdmin(AbstractGuiAdmin gui) {
		this.gui = gui;
		this.invOfGui = null;
	}

	public CardComparator getCardComparator() {
		return cardComparator;
	}

	public void setCardComparator(CardComparator cardComparator) {
		this.cardComparator = cardComparator;
	}

	public int getPageEditGui() {
		return pageEditGui;
	}

	public void setPageEditGui(int pageEditGui) {
		this.pageEditGui = pageEditGui;
	}

	public Inventory getInvOfGui() {
		return invOfGui;
	}
	
	public void setInvOfGui(Inventory invOfGui) {
		this.invOfGui = invOfGui;
	}
	
}
