package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartFaction;

public class BookGuiEditFaction extends BookGuiEditEnum<CartFaction> {

	public BookGuiEditFaction(EditCartsGui ec) {
		super(ec, CartFaction.DARK, BookGuis.FACTION);
	}

	@Override
	protected String valueEnum(Cart cart) {
		return cart.getFaction().name();
	}
	
	@Override
	protected void setEnum(Cart cart, CartFaction enum1, PlayerEditCart player) {
		cart.setFaction(enum1);
		this.ec.sorci.getCarts().update(cart);
		this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
	}

}
