package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartComparator;
import fr.tangv.sorcicubespell.util.Gui;

public class PlayerEditCart {

	private Player player;
	private Cart cart;
	private Gui guiOpened;
	private Gui gui;
	private CartComparator cartComparator;
	private int pageEditGui;
	private boolean insertLink;
	
	public PlayerEditCart(Player player) {
		this.player = player;
		this.cart = null;
		this.guiOpened = null;
		this.gui = null;
		this.cartComparator = CartComparator.BY_ID;
		this.pageEditGui = 0;
		this.insertLink = false;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
		this.insertLink = false;
	}
	
	public Gui getGui() {
		return gui;
	}
	
	public void setGui(Gui gui) {
		this.gui = gui;
	}

	public Gui getGuiOpened() {
		return guiOpened;
	}

	public void setGuiOpened(Gui guiOpened) {
		this.guiOpened = guiOpened;
	}

	public CartComparator getCartComparator() {
		return cartComparator;
	}

	public void setCartComparator(CartComparator cartComparator) {
		this.cartComparator = cartComparator;
	}

	public int getPageEditGui() {
		return pageEditGui;
	}

	public void setPageEditGui(int pageEditGui) {
		this.pageEditGui = pageEditGui;
	}

	public boolean isInsertLink() {
		return insertLink;
	}

	public void setInsertLink(boolean insertLink) {
		this.insertLink = insertLink;
	}

}
