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
