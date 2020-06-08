package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.CartComparator;

public class BookGuiPlayerEditSort extends BookGuiEnumPlayer<CartComparator> {

	public BookGuiPlayerEditSort(EditCartsGui ec) {
		super(ec, CartComparator.BY_ID, "sort");
	}

	@Override
	protected String valueEnum(PlayerEditCart player) {
		return player.getCartComparator().name();
	}

	@Override
	protected void setEnum(PlayerEditCart player, CartComparator enum1) {
		player.setCartComparator(enum1);
		this.back(player);
	}

	@Override
	protected void back(PlayerEditCart player) {
		this.ec.guiEditList.open(player.getPlayer());
	}
	
}
