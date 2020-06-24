package fr.tangv.sorcicubespell.card;

import java.util.Comparator;
import java.util.regex.Pattern;

public enum CardComparator implements Comparator<Card> {

	BY_ID(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return c1.getUUID().compareTo(c2.getUUID());
		}
	}),
	BY_FACTION(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return Integer.compare(c1.getFaction().ordinal(), c2.getFaction().ordinal());
		}
	}),
	BY_RARITY(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return Integer.compare(c1.getRarity().ordinal(), c2.getRarity().ordinal());
		}
	}),
	BY_TYPE(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return Integer.compare(c1.getType().ordinal(), c2.getType().ordinal());
		}
	}),
	BY_LOW_MANA(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return Integer.compare(c1.getMana(), c2.getMana());
		}
	}),
	BY_HIGH_MANA(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			return Integer.compare(c2.getMana(), c1.getMana());
		}
	}),
	BY_NAME(new Comparator<Card>() {
		@Override
		public int compare(Card c1, Card c2) {
			Pattern p = Pattern.compile("§.");
			String n1 = p.matcher(c1.getName()).replaceAll("");
			String n2 = p.matcher(c2.getName()).replaceAll("");
			return n1.compareTo(n2);
		}
	});
	
	private Comparator<Card> comparatorCart;
	
	private CardComparator(Comparator<Card> comparatorCart) {
		this.comparatorCart = comparatorCart;
	}

	@Override
	public int compare(Card c1, Card c2) {
		return this.comparatorCart.compare(c1, c2);
	}
	
}
