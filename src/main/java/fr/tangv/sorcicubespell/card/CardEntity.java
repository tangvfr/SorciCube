package fr.tangv.sorcicubespell.card;

public class CardEntity {

	private Card card;
	private CardFeature health;
	private CardFeature attack;
	private boolean hasIncitement;
	private CardSkin skin;
	
	public CardEntity(Card card) throws Exception {
		if (card.getType() != CardType.ENTITY)
			throw new Exception("Card is not ENTITY !");
		this.card = card;
		CardFeatures features = card.getFeatures();
		this.skin = features.hasFeature(CardFeatureType.SKIN) ?
				features.getFeature(CardFeatureType.SKIN).getValue().asSkin()
				: null;
				
		this.health = features.getFeature(CardFeatureType.HEALTH);
		this.attack = features.getFeature(CardFeatureType.DAMAGE);
		this.hasIncitement = features.hasFeature(CardFeatureType.INCITEMENT);
	}
	
	public String getName() {
		return card.getName();
	}
	
	public int getHealth() {
		return this.health.getValue().asInt();
	}
	
	public int getAttack() {
		return this.attack.getValue().asInt();
	}
	
	public void setHealth(int health) {
		this.health.setValue(new CardValue(health));
	}
	
	public void setAttack(int attack) {
		this.attack.setValue(new CardValue(attack));
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
		return hasIncitement;
	}
	
	public CardSkin getSkin() {
		return skin;
	}
	
	public Card getCard() {
		return card;
	}
	
}
