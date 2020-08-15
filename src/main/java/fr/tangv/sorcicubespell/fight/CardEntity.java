package fr.tangv.sorcicubespell.fight;

import java.util.UUID;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardFeatures;
import fr.tangv.sorcicubespell.card.CardSkin;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.card.CardValue;

public class CardEntity {

	private final Card card;
	private final CardSkin skin;
	
	public CardEntity(Card card) throws Exception {
		if (card.getType() != CardType.ENTITY)
			throw new Exception("Card is not ENTITY !");
		this.card = card;
		CardFeatures features = card.getFeatures();
		this.skin = features.hasFeature(CardFeatureType.SKIN) ?
				features.getFeature(CardFeatureType.SKIN).getValue().asSkin()
				: null;
	}
	
	public String getName() {
		return card.getName();
	}
	
	public int getHealth() {
		return this.card.getFeatures().getFeature(CardFeatureType.HEALTH).getValue().asInt();
	}
	
	public int getAttack() {
		return this.card.getFeatures().getFeature(CardFeatureType.DAMAGE).getValue().asInt();
	}
	
	public void setHealth(int health) {
		this.card.getFeatures().getFeature(CardFeatureType.HEALTH).setValue(new CardValue(health));
	}
	
	public void setAttack(int attack) {
		this.card.getFeatures().getFeature(CardFeatureType.DAMAGE).setValue(new CardValue(attack));
	}
	
	private Card featureToCard(SorciCubeSpell sorci, CardFeature feature) {
		return sorci.getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
	}
	
	private CardFeature featureToGiveCardFeature(SorciCubeSpell sorci, CardFeature feature) {
		return new CardFeature(CardFeatureType.GIVE_FEATURE_CART, feature.getValue());
	}
	
	public boolean hasIfAEO() {
		return this.card.getFeatures().hasFeature(CardFeatureType.IF_ATTACKED_EXEC_ONE);
	}
	
	public boolean hasIfAE() {
		return this.card.getFeatures().hasFeature(CardFeatureType.IF_ATTACKED_EXEC);
	}
	
	public boolean hasIfAGO() {
		return this.card.getFeatures().hasFeature(CardFeatureType.IF_ATTACKED_GIVE_ONE);
	}
	
	public boolean hasIfAG() {
		return this.card.getFeatures().hasFeature(CardFeatureType.IF_ATTACKED_GIVE);
	}
	
	public Card getIfAEO(SorciCubeSpell sorci) {
		return featureToCard(sorci, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC_ONE));
	}
	
	public Card getIfAE(SorciCubeSpell sorci) {
		return featureToCard(sorci, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC));
	}
	
	public CardFeature getIfAGO(SorciCubeSpell sorci) {
		return featureToGiveCardFeature(sorci, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE_ONE));
	}
	
	public CardFeature getIfAG(SorciCubeSpell sorci) {
		return featureToGiveCardFeature(sorci, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE));
	}

	public boolean hasActionSpawn() {
		return this.card.getFeatures().hasFeature(CardFeatureType.ACTION_SPAWN);
	}
	
	public boolean hasActionDead() {
		return this.card.getFeatures().hasFeature(CardFeatureType.ACTION_DEAD);
	}
	
	public Card getActionSpawn(SorciCubeSpell sorci) {
		return featureToCard(sorci, card.getFeatures().getFeature(CardFeatureType.ACTION_SPAWN));
	}
	
	public Card getActionDead(SorciCubeSpell sorci) {
		return featureToCard(sorci, card.getFeatures().getFeature(CardFeatureType.ACTION_DEAD));
	}
	
	public boolean isInvulnerability() {
		return this.card.getFeatures().hasFeature(CardFeatureType.INVULNERABILITY);
	}
	
	public boolean isImmobilization() {
		return this.card.getFeatures().hasFeature(CardFeatureType.IMMOBILIZATION);
	}
	
	public boolean isStunned() {
		return this.card.getFeatures().hasFeature(CardFeatureType.STUNNED);
	}
	
	public void nextRound(CardFeatureType type) {
		if (card.getFeatures().hasFeature(type)) {
			CardFeature feature = card.getFeatures().getFeature(type);
			int value = feature.getValue().asInt();
			if (value > 0) {
				feature.setValue(new CardValue(value-1));
			} else {
				card.getFeatures().removeFeature(type);
			}
		}
	}
	
	public void nextRound() {
		nextRound(CardFeatureType.INVULNERABILITY);
		nextRound(CardFeatureType.IMMOBILIZATION);
		nextRound(CardFeatureType.STUNNED);
	}
	
	public boolean hasSkin() {
		return skin != null;
	}
	
	public boolean hasIncitement() {
		return this.card.getFeatures().hasFeature(CardFeatureType.INCITEMENT);
	}
	
	public CardSkin getSkin() {
		return skin;
	}
	
	public Card getCard() {
		return card;
	}
	
}
