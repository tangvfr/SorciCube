package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;

public class AnvilEditName extends AnvilEdit {
	
	public AnvilEditName(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
		super(player, "name", cart.getName(), cart, bgec);
	}
	
	@Override
	public void valid(String string) {
		this.cart.setName(string);
		this.bgec.ec.sorci.getCarts().update(this.cart);
		this.back();
	}

}
