package fr.tangv.sorcicubespell.util;

import fr.tangv.sorcicubespell.card.CardFaction;

public class SkullUrl {

	public static String TRASH = "http://textures.minecraft.net/texture/f32c1472a7bc6975ded7c0c51696959b89af61b75ae954cc4036bc384b3b8301";
	public static String BACK_RED = "http://textures.minecraft.net/texture/edf5c2f893bd3f89ca40703ded3e42dd0fbdba6f6768c8789afdff1fa78bf6";
	public static String BACK_GRAY = "http://textures.minecraft.net/texture/74133f6ac3be2e2499a784efadcfffeb9ace025c3646ada67f3414e5ef3394";
	public static String FORWARD_GRAY = "http://textures.minecraft.net/texture/e02fa3b2dcb11c6639cc9b9146bea54fbc6646d855bdde1dc6435124a11215d";
	public static String X_RED = "http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7";
	public static String HOPPER = "http://textures.minecraft.net/texture/8eb81ef8902379650ba79f45723d6b9c888388a00fc4e192f3454fe193882ee1";
	public static String BLACK_BERRY = "http://textures.minecraft.net/texture/914fb0fcaa4413a3d69de530e1674942383b630ca3565fbae08a8a00e81e371b";
	public static String ENDER_ORB = "http://textures.minecraft.net/texture/ff778f72eaca452ee013893e2bc53d8d5b1fca44cfe2703865b054c28a3dd7";
	public static String BALLOON_YELLOW = "http://textures.minecraft.net/texture/a7f381a20a9c640428077070cc7bd95d688592d1104ccbcd713649a49e41ebfb";
	public static String LECTTUCE = "http://textures.minecraft.net/texture/ffcf698f77d6783ee072af7ca4e87757f713dc1c941a7e75583f2ea5165d57d";
	public static String CHEST_GRAY = "http://textures.minecraft.net/texture/5f93ed3f196876113b2e7460933493b81da91b8f34db3c5288a69eeb96de40fb";
	public static String CHEST_GREEN = "http://textures.minecraft.net/texture/c31b161770f020237b4aa1e928c5480c8b36cbfe3a6aab9e22e557c1d910";
	
	public static String getSkullForFaction(CardFaction faction, boolean basic) {
		switch (faction) {
			case BASIC:
				if (basic)
					return SkullUrl.HOPPER;
				else
					return SkullUrl.CHEST_GREEN;
	
			case DARK:
				return SkullUrl.ENDER_ORB;
				
			case LIGHT:
				return SkullUrl.BALLOON_YELLOW;
				
			case NATURE:
				return SkullUrl.LECTTUCE;
			
			case TOXIC:
				return SkullUrl.BLACK_BERRY;
		}
		return SkullUrl.X_RED;
	}
	
}
