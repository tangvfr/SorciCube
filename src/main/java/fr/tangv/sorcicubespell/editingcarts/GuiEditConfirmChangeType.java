package fr.tangv.sorcicubespell.editingcarts;

public class GuiEditConfirmChangeType extends GuiEditConfirm {

	public GuiEditConfirmChangeType(EditCartsGui ec) {
		super(ec, "change_type");
	}

	@Override
	protected void valid(PlayerEditCart player) {
		this.ec.guiBooks.get(BookGuis.TYPE).open(player, player.getCart());
	}

	@Override
	protected void cancel(PlayerEditCart player) {
		this.ec.guiBooks.get(BookGuis.MAIN).open(player, player.getCart());
	}
	
}
