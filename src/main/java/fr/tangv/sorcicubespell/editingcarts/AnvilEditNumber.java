package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;

public abstract class AnvilEditNumber extends AnvilEdit {

	private BookGuiEditCart bgec;
	private Cart cart;
	private boolean nonable;
	private int min;

	public AnvilEditNumber(PlayerEditCart player, String name, int value, Cart cart, BookGuiEditCart bgec, boolean nonable, int min) {
		super(player, name, ""+value, bgec.ec.sorci);
		this.bgec = bgec;
		this.cart = cart;
		this.nonable = nonable;
		this.min = min;
	}

	public abstract void setNumber(Cart cart, int newNumber);
	
	@Override
	public void valid(String string) {
		try {
			int number;
			//define number
			if (string.equalsIgnoreCase("none"))
				number = -1;
			else
				number = Integer.parseInt(string);
			//test nonable
			if (number < 0)
				if (nonable)
					number = -1;
				else
					throw new Exception("Is not nonable");
			//test min
			else if (number <= min)
				throw new Exception("Number lower minimal");
			//seting
			this.setNumber(cart, number);
			this.sorci.getCarts().update(this.cart);
			this.back();
			return;
		} catch (Exception e) {}
		this.open();
	}
	
	@Override
	public void back() {
		this.bgec.open(this.player, this.cart);
	}
	
}
