package fr.tangv.sorcicubespell.carts;

import java.util.Comparator;

public enum CartComparator implements Comparator<Cart> {

	BY_ID(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return c1.getId().compareTo(c2.getId());
		}
	});
	
	private Comparator<Cart> comparatorCart;
	
	private CartComparator(Comparator<Cart> comparatorCart) {
		this.comparatorCart = comparatorCart;
	}

	@Override
	public int compare(Cart c1, Cart c2) {
		return this.comparatorCart.compare(c1, c2);
	}
	
}
