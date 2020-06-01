package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartSort;

public class BookGuiEditCible extends BookGuiEditEnum<CartCible> {

	public BookGuiEditCible(EditCartsGui ec) {
		super(ec, CartCible.ALL, BookGuis.CIBLE);
	}
	
	@Override
	protected String valueEnum(Cart cart) {
		if (cart instanceof CartSort)
			return ((CartSort) cart).getCible().name();
		else
			return "";
	}
	
	@Override
	protected void setEnum(Cart cart, CartCible enum1, PlayerEditCart player) {
		if (cart instanceof CartSort) {
			((CartSort) cart).setCible(enum1);
			this.ec.sorci.getCarts().update(cart);
			this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
		}
	}
	
}
