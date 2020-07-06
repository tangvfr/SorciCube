package fr.tangv.sorcicubespell.util;

import fr.tangv.sorcicubespell.card.CardFaction;

public class SkullUrl {

	public static final String TRASH = "http://textures.minecraft.net/texture/f32c1472a7bc6975ded7c0c51696959b89af61b75ae954cc4036bc384b3b8301";
	public static final String BACK_RED = "http://textures.minecraft.net/texture/edf5c2f893bd3f89ca40703ded3e42dd0fbdba6f6768c8789afdff1fa78bf6";
	public static final String BACK_GRAY = "http://textures.minecraft.net/texture/74133f6ac3be2e2499a784efadcfffeb9ace025c3646ada67f3414e5ef3394";
	public static final String FORWARD_GRAY = "http://textures.minecraft.net/texture/e02fa3b2dcb11c6639cc9b9146bea54fbc6646d855bdde1dc6435124a11215d";
	public static final String X_RED = "http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7";
	public static final String HOPPER = "http://textures.minecraft.net/texture/8eb81ef8902379650ba79f45723d6b9c888388a00fc4e192f3454fe193882ee1";
	public static final String BLACK_BERRY = "http://textures.minecraft.net/texture/914fb0fcaa4413a3d69de530e1674942383b630ca3565fbae08a8a00e81e371b";
	public static final String ENDER_ORB = "http://textures.minecraft.net/texture/ff778f72eaca452ee013893e2bc53d8d5b1fca44cfe2703865b054c28a3dd7";
	public static final String BALLOON_YELLOW = "http://textures.minecraft.net/texture/a7f381a20a9c640428077070cc7bd95d688592d1104ccbcd713649a49e41ebfb";
	public static final String LECTTUCE = "http://textures.minecraft.net/texture/ffcf698f77d6783ee072af7ca4e87757f713dc1c941a7e75583f2ea5165d57d";
	public static final String CHEST_GRAY = "http://textures.minecraft.net/texture/5f93ed3f196876113b2e7460933493b81da91b8f34db3c5288a69eeb96de40fb";
	public static final String CHEST_GREEN = "http://textures.minecraft.net/texture/c31b161770f020237b4aa1e928c5480c8b36cbfe3a6aab9e22e557c1d910";
	public static final String UP_BLUE = "http://textures.minecraft.net/texture/f2fc23866523caaa8a9534566127a6f8389af3e76b8e3c33c2473cba6889c4";
	public static final String DOWN_BLUE = "http://textures.minecraft.net/texture/f0d1df8046f0b5d934c3e05798eacfeea6d7b595dbe26debf7db9acc8c4fa798";
	public static final String I_DRAY = "http://textures.minecraft.net/texture/e7a2f8bf7e138d6ba9246f7dc1ff144f43ed512e44892b1e316535ea882f7a5";
	public static final String N_RED = "http://textures.minecraft.net/texture/3a1d1f6fec429e7cbabc6965b03285ec5ac918ee6e5c3511aef81f52a848e71";
	public static final String T_PURPLE = "http://textures.minecraft.net/texture/c451897d7f747a901c183bfee2ed174f35655c966f9adf6e2c7630a03a8155bd";
	public static final String R_YELLOW = "http://textures.minecraft.net/texture/6ac6f775e8acfdf6e1deac80a8da1f37b3b4ba5caa4a35249f9eb145ccf43da5";
	public static final String F_LIME = "http://textures.minecraft.net/texture/a4413bdb2a7ea27cf303d799ddc627d6feed71b01876b9b1b0e013a8e1c624";
	public static final String QUESTION = "http://textures.minecraft.net/texture/9d9cc58ad25a1ab16d36bb5d6d493c8f5898c2bf302b64e325921c41c35867";
	public static final String N_GRAY = "http://textures.minecraft.net/texture/651a7ee66f60237419917fdc75f5d341cfbb9f7ccaaef3aff2af419d588e701a";
	
	public static final String getSkullForFaction(CardFaction faction) {
		switch (faction) {
			case BASIC:
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
