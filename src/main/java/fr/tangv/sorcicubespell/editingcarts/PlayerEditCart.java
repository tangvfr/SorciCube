package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.util.Gui;

public class PlayerEditCart {

	private Player player;
	private Cart cart;
	private Gui guiOpened;
	private Gui gui;
	
	public PlayerEditCart(Player player) {
		this.player = player;
		this.cart = null;
		this.guiOpened = null;
		this.gui = null;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
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
	
}
