package fr.tangv.sorcicubecore.card;

import fr.tangv.sorcicubecore.card.CardValue.TypeValue;

public enum CardFeatureType {
	
	SKIN(TypeValue.SKIN, false),
	HEALTH(TypeValue.NUMBER, false),
	DAMAGE(TypeValue.NUMBER, true),
	DESTRUCT(TypeValue.NONE, true),
	TAKE_NEW_CARD(TypeValue.NUMBER, true),
	COPY_CARD_ARENA_POSE(TypeValue.NUMBER, true),
	COPY_CARD_ARENA(TypeValue.NUMBER, true),
	HEAL(TypeValue.NUMBER, true),
	BOOST_DAMAGE(TypeValue.NUMBER, true),
	BOOST_HEALTH(TypeValue.NUMBER, true),
	BOOST_MANA(TypeValue.NUMBER, true),
	REMOVE_MANA_HERO(TypeValue.NUMBER, true),
	INCITEMENT(TypeValue.NONE, true),
	EXCITED(TypeValue.NONE, true),
	INVULNERABILITY(TypeValue.ROUND, true),
	IMMOBILIZATION(TypeValue.ROUND, true),
	STUNNED(TypeValue.ROUND, true),
	GIVE_CARD(TypeValue.UUID, true),
	IF_ATTACKED_EXEC_ONE(TypeValue.UUID, true),
	IF_ATTACKED_EXEC(TypeValue.UUID, true),
	IF_ATTACKED_GIVE_ONE(TypeValue.UUID, true),
	IF_ATTACKED_GIVE(TypeValue.UUID, true),
	INVOCATION(TypeValue.UUID, true),
	ACTION_SPAWN(TypeValue.UUID, true),
	ACTION_DEAD(TypeValue.UUID, true),
	EXECUTE(TypeValue.UUID, true),
	APPLY_EXCITED(TypeValue.NONE, true),
	GIVE_FEATURE_CARD(TypeValue.UUID, true),
	METAMORPH_TO(TypeValue.UUID, true),
	HIDE_CARD(TypeValue.NONE, true);
	
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
