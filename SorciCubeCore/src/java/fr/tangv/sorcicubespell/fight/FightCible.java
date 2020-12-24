package fr.tangv.sorcicubespell.fight;

public enum FightCible {

	HERO_ALLY(true, true),
	ENTITY_1_ALLY(false, true),
	ENTITY_2_ALLY(false, true),
	ENTITY_3_ALLY(false, true),
	ENTITY_4_ALLY(false, true),
	ENTITY_5_ALLY(false, true),	
	HERO_ENEMIE(true, false),
	ENTITY_1_ENEMIE(false, false),
	ENTITY_2_ENEMIE(false, false),
	ENTITY_3_ENEMIE(false, false),
	ENTITY_4_ENEMIE(false, false),
	ENTITY_5_ENEMIE(false, false);
	
	private final boolean ally;
	private final boolean hero;
	
	private FightCible(boolean hero, boolean ally) {
		this.hero = hero;
		this.ally = ally;
	}
	
	public boolean isHero() {
		return hero;
	}
	
	public boolean isAlly() {
		return ally;
	}
	
}
