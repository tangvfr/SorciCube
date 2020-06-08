package fr.tangv.sorcicubespell.editingcarts;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartEntity;
import fr.tangv.sorcicubespell.carts.CartSort;

public class AnvilEditNumberCart {

	//for all cart
	
	public static class AnvilEditCountMana extends AnvilEditNumber {
		
		public AnvilEditCountMana(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
			super(player, "count_mana", cart.getCountMana(),
					cart, bgec, false, 0);
		}

		@Override
		public void setNumber(Cart cart, int newNumber) {
			cart.setCountMana(newNumber);
		}
			
	}
	
	public static class AnvilEditDamage extends AnvilEditNumber {
		
		public AnvilEditDamage(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
			super(player, "damage", cart.getDamage(),
					cart, bgec, true, 1);
		}

		@Override
		public void setNumber(Cart cart, int newNumber) {
			cart.setDamage(newNumber);
		}
			
	}
	
	//for entity cart
	
	public static class AnvilEditHealth extends AnvilEditNumber {
		
		public AnvilEditHealth(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
			super(player, "health", ((CartEntity) cart).getHealth(),
					cart, bgec, false, 1);
		}

		@Override
		public void setNumber(Cart cart, int newNumber) {
			((CartEntity) cart).setHealth(newNumber);
		}
			
	}
	
	//for sort cart
	
	public static class AnvilEditHeal extends AnvilEditNumber {
		
		public AnvilEditHeal(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
			super(player, "heal", ((CartSort) cart).getHeal(),
					cart, bgec, true, 1);
		}

		@Override
		public void setNumber(Cart cart, int newNumber) {
			((CartSort) cart).setHeal(newNumber);
		}
			
	}
	
	public static class AnvilEditGiveMana extends AnvilEditNumber {
		
		public AnvilEditGiveMana(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
			super(player, "give_mana", ((CartSort) cart).getGiveMana(),
					cart, bgec, true, 1);
		}

		@Override
		public void setNumber(Cart cart, int newNumber) {
			((CartSort) cart).setGiveMana(newNumber);
		}
			
	}

}
