package fr.tangv.sorcicubespell.card;

import fr.tangv.sorcicubespell.card.CardValue.TypeValue;

public enum CardFeatureType {
	
	SKIN(TypeValue.SKIN, false),
	HEALTH(TypeValue.NUMBER, false),
	DAMAGE(TypeValue.NUMBER, true),
	DESTRUCT(TypeValue.NONE, true),
	TAKE_NEW_CART(TypeValue.NUMBER, true),
	COPY_CART_ARENA_POSE(TypeValue.NUMBER, true),
	COPY_CART_ARENA(TypeValue.NUMBER, true),
	HEAL(TypeValue.NUMBER, true),
	BOOST_DAMAGE(TypeValue.NUMBER, true),
	BOOST_HEALTH(TypeValue.NUMBER, true),
	BOOST_MANA(TypeValue.NUMBER, true),
	REMOVE_MANA_HERO(TypeValue.NUMBER, true),
	INCITEMENT(TypeValue.NONE, true),
	EXCITED(TypeValue.NONE, true),
	INVULNERABILITY(TypeValue.NUMBER, true),
	IMMOBILIZATION(TypeValue.NUMBER, true),
	STUNNED(TypeValue.NUMBER, true),
	IF_ATTACKED_EXEC_ONE(TypeValue.TEXT, true),
	IF_ATTACKED_EXEC(TypeValue.TEXT, true),
	IF_ATTACKED_GIVE_ONE(TypeValue.TEXT, true),
	IF_ATTACKED_GIVE(TypeValue.TEXT, true),
	INVOCATION(TypeValue.TEXT, true),
	ACTION_SPAWN(TypeValue.TEXT, true),
	ACTION_DEAD(TypeValue.TEXT, true),
	EXECUTE(TypeValue.TEXT, true),
	APPLY_EXCITED(TypeValue.NONE, true),
	GIVE_FEATURE_CART(TypeValue.TEXT, true),
	METAMORPH_TO(TypeValue.TEXT, true),
	HIDE_CART(TypeValue.NONE, true);
	
	private final TypeValue typeValue;
	private final boolean show;
	
	private CardFeatureType(TypeValue typeValue, boolean show) {
		this.typeValue = typeValue;
		this.show = show;
	}
	
	public TypeValue getTypeValue() {
		return typeValue;
	}
	
	public boolean isShow() {
		return show;
	}
	
}
