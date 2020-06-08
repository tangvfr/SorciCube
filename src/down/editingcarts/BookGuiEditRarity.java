package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartRarity;

public class BookGuiEditRarity extends BookGuiEditEnum<CartRarity> {
	
	public BookGuiEditRarity(EditCartsGui ec) {
		super(ec, CartRarity.COMMUN, BookGuis.RARITY);
	}
	
	@Override
	protected String valueEnum(Cart cart) {
		return cart.getRarity().name();
	}
	
	@Override
	protected void setEnum(Cart cart, CartRarity enum1, PlayerEditCart player) {
		cart.setRarity(enum1);
		this.ec.sorci.getCarts().update(cart);
		this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
	}
		
}
