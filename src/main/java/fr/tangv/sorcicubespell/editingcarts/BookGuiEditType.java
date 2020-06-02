package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartEntity;
import fr.tangv.sorcicubespell.carts.CartSort;
import fr.tangv.sorcicubespell.carts.CartType;

public class BookGuiEditType extends BookGuiEditEnum<CartType> {

	public BookGuiEditType(EditCartsGui ec) {
		super(ec, CartType.ENTITY, BookGuis.TYPE);
	}

	@Override
	protected String valueEnum(Cart cart) {
		return cart.getType().name();
	}
	
	@Override
	protected void setEnum(Cart cart, CartType enum1, PlayerEditCart player) {
		Cart newCart;
		if (enum1 == CartType.ENTITY) {
			newCart = new CartEntity(cart.getId(),
					cart.getMaterial(),
					cart.getMaterialURL(),
					cart.getName(),
					cart.getDescription(),
					cart.getCountMana(),
					cart.getDamage(),
					cart.getRarity(),
					cart.getFaction(),
					1);
		} else {
			newCart = new CartSort(cart.getId(),
					cart.getMaterial(),
					cart.getMaterialURL(),
					cart.getName(),
					cart.getDescription(),
					cart.getCountMana(),
					cart.getDamage(),
					cart.getRarity(),
					cart.getFaction(),
					-1,
					-1,
					CartCible.ONE_ENEMIE);
		}
		this.ec.sorci.getCarts().update(newCart);
		this.ec.guiBooks.get(BookGuis.MAIN).open(player, newCart);
	}
	
}