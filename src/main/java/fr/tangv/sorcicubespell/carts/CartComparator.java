package fr.tangv.sorcicubespell.carts;

import java.util.Comparator;
import java.util.regex.Pattern;

public enum CartComparator implements Comparator<Cart> {

	BY_ID(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return c1.getId().compareTo(c2.getId());
		}
	}),
	BY_FACTION(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c1.getFaction().ordinal(), c2.getFaction().ordinal());
		}
	}),
	BY_RARITY(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c1.getRarity().ordinal(), c2.getRarity().ordinal());
		}
	}),
	BY_TYPE(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c1.getType().ordinal(), c2.getType().ordinal());
		}
	}),
	BY_LOW_DAMAGE(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c1.getDamage(), c2.getDamage());
		}
	}),
	BY_HIGH_DAMAGE(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c2.getDamage(), c1.getDamage());
		}
	}),
	BY_LOW_MANA(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c1.getCountMana(), c2.getCountMana());
		}
	}),
	BY_HIGH_MANA(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			return Integer.compare(c2.getCountMana(), c1.getCountMana());
		}
	}),
	BY_NAME(new Comparator<Cart>() {
		@Override
		public int compare(Cart c1, Cart c2) {
			Pattern p = Pattern.compile("§.");
			String n1 = p.matcher(c1.getName()).replaceAll("");
			String n2 = p.matcher(c2.getName()).replaceAll("");
			return n1.compareTo(n2);
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
