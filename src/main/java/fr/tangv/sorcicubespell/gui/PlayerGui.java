package fr.tangv.sorcicubespell.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardComparator;

public class PlayerGui {

	private Player player;
	private Card card;
	private Inventory invOfGui;
	private AbstractGui gui;
	private CardComparator cardComparator;
	private int pageEditGui;
	
	public PlayerGui(Player player) {
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
	
	public AbstractGui getGui() {
		return gui;
	}
	
	public void setGuiAdmin(AbstractGui gui) {
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
