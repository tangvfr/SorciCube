package fr.tangv.sorcicubespell.card;

public enum CardCible {

	NONE(false),
	ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE(true),
	ONE_HERO(true),
	ALL_HERO(false),
	ONE(true),
	ONE_ENEMIE(true),
	ONE_ENTITY_ENEMIE(true),
	HERO_ENEMIE(false),
	ALL_ENEMIE(false),
	ALL_ENTITY_ENEMIE(false),
	ONE_ALLY(true),
	ONE_ENTITY_ALLY(true),
	HERO_ALLY(false),
	ALL_ALLY(false),
	ALL_ENTITY_ALLY(false),
	ONE_ENTITY(true),
	ALL_ENTITY(false),
	ALL(false);
	
	private boolean choose;
	
	private CardCible(boolean choose) {
		this.choose = choose;
	}
	
	public boolean hasChoose() {
		return choose;
	}
	
}
