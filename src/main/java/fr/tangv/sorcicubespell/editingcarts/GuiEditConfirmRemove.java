package fr.tangv.sorcicubespell.editingcarts;

public class GuiEditConfirmRemove extends GuiEditConfirm {

	public GuiEditConfirmRemove(EditCartsGui ec) {
		super(ec, "remove");
	}

	@Override
	protected void valid(PlayerEditCart player) {
		this.ec.sorci.getCarts().delete(player.getCart());
		this.ec.guiEditList.open(player.getPlayer());
	}

	@Override
	protected void cancel(PlayerEditCart player) {
		this.ec.guiBooks.get(BookGuis.MAIN).open(player, player.getCart());
	}
	
}
