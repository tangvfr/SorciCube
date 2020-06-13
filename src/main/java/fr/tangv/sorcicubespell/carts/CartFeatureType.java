package fr.tangv.sorcicubespell.carts;

import fr.tangv.sorcicubespell.carts.CartValue.TypeValue;

public enum CartFeatureType {
	
	SKIN(TypeValue.TEXT),
	HEALTH(TypeValue.NUMBER),
	DAMAGE(TypeValue.NUMBER),
	HIDE_CART(TypeValue.NONE),
	IF_HURT(TypeValue.NONE),
	TAKE_NEW_CART(TypeValue.NONE),
	DESTRUCT(TypeValue.NONE),
	HEAL(TypeValue.NUMBER),
	BOOST_DAMAGE(TypeValue.NUMBER),
	BOOST_HEALTH(TypeValue.NUMBER),
	BOOST_MANA(TypeValue.NUMBER),
	COPY_CART_ARENA(TypeValue.NUMBER),
	REMOVE_MANA_HERO(TypeValue.NUMBER),
	INVULNERABILITY(TypeValue.NUMBER),
	IMMOBILIZATION(TypeValue.NUMBER),
	INVOCATION(TypeValue.TEXT),
	ACTION_DEAD(TypeValue.TEXT),
	ACTION_SPAWN(TypeValue.TEXT),
	METAMORPH_TO(TypeValue.TEXT),
	GIVE_FEATURE_CART(TypeValue.TEXT);
	
	private TypeValue typeValue;
	
	private CartFeatureType(TypeValue typeValue) {
		this.typeValue = typeValue;
	}
	
	public TypeValue getTypeValue() {
		return typeValue;
	}
	
}
