package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.inventory.meta.BookMeta;

public class BookGuiEditCart extends BookGui {

	public BookGuiEditCart(EditCartsGui ec) {
		super(ec, "editcart");
	}

	@Override
	protected BookMeta getBook(PlayerEditCart player, BookMeta meta) {
		
		
		
		return null;
	}

	@Override
	protected boolean onCommand(PlayerEditCart player, String[] args) {
		
		return false;
	}

}
