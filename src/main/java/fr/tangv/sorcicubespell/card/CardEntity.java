package fr.tangv.sorcicubespell.card;

public class CardEntity {

	private Card card;
	private CardFeature health;
	private CardFeature attack;
	private String skin;
	
	public CardEntity(Card card) throws Exception {
		if (card.getType() != CardType.ENTITY)
			throw new Exception("Card is not ENTITY !");
		this.card = card;
		this.skin = null;
		for (CardFeature feature : card.getFeatures().listFeatures())
			if (feature.getType() == CardFeatureType.SKIN) {
				skin = feature.getValue().asString();
				break;
			}
		this.health = card.getFeatures().getFeature(CardFeatures.HEALTH);
		this.attack = card.getFeatures().getFeature(CardFeatures.ATTACK_DAMMAGE);
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
	
	public String getSkin() {
		return skin;
	}
	
	public Card getCard() {
		return card;
	}
	
}
