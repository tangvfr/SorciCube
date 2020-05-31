package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;

public class AnvilEditName extends AnvilEdit {

	private BookGuiEditCart bgec;
	private Cart cart;
	
	public AnvilEditName(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
		super(player, "name", cart.getName(), bgec.ec.sorci);
		this.bgec = bgec;
		this.cart = cart;
	}
	
	@Override
	public void valid(String string) {
		this.cart.setName(string);
		this.sorci.getCarts().update(this.cart);
		this.back();
	}
	
	@Override
	public void back() {
		this.bgec.open(player, cart);
	}
	
}
