package fr.tangv.sorcicubespell.fight;

import java.util.Vector;

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
	private final boolean[] actions;
	
	public CardEntity(Card card) throws Exception {
		if (card.getType() != CardType.ENTITY)
			throw new Exception("Card is not ENTITY !");
		this.card = card;
		CardFeatures features = card.getFeatures();
		this.skin = features.hasFeature(CardFeatureType.SKIN) ?
				features.getFeature(CardFeatureType.SKIN).getValue().asSkin()
				: null;
		this.actions = new boolean[6];
	}
	
	public int getHealth() {
		return this.card.getFeatures().getFeature(CardFeatureType.HEALTH).getValue().asNumber();
	}
	
	public int getAttack() {
		return this.card.getFeatures().getFeature(CardFeatureType.DAMAGE).getValue().asNumber();
	}
	
	public void setHealth(int health) {
		this.card.getFeatures().getFeature(CardFeatureType.HEALTH).setValue(new CardValue(health));
	}
	
	public void setAttack(int attack) {
		this.card.getFeatures().getFeature(CardFeatureType.DAMAGE).setValue(new CardValue(attack));
	}
	
	private String actioneCard(PlayerFight owner, CardFeature feature) {
		Card card = owner.getFight().getSorci().getManagerCards().getCard(feature.getValue().asUUID());
		if (card != null) {
			Vector<FightHead> heads = FightCible.randomFightHeadsForCible(owner, card.getCible(), card.getCibleFaction());
			if (heads.isEmpty())
				return null;
			FightSpell.startActionSpell(owner, card.getFeatures(), heads);
			return card.renderName();
		}
		return "nothing";
	}
	
	private void giveCard(PlayerFight owner, FightEntity entity, CardFeature feature) {
		FightSpell.startActionFeature(owner, new CardFeature(CardFeatureType.GIVE_FEATURE_CARD, feature.getValue()), entity);
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
	
	public boolean hasActionSpawn() {
		return this.card.getFeatures().hasFeature(CardFeatureType.ACTION_SPAWN);
	}
	
	public boolean hasActionDead() {
		return this.card.getFeatures().hasFeature(CardFeatureType.ACTION_DEAD);
	}
	
	public void excutingIfAEO() {
		this.actions[0] = true;
	}
	
	public void excutingIfAE() {
		this.actions[1] = true;
	}
	
	public void excutingIfAGO() {
		this.actions[2] = true;
	}
	
	public void excutingIfAG() {
		this.actions[3] = true;
	}
	
	public void excutingActionSpawn() {
		this.actions[4] = true;
	}
	
	public void excutingActionDead() {
		this.actions[5] = true;
	}
	
	public void excuteAction(FightEntity entity) {
		PlayerFight player = entity.getOwner();
		if (actions[0]) {
			actions[0] = false;
			actioneCard(player, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC_ONE));
		}
		if (actions[1]) {
			actions[1] = false;
			actioneCard(player, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC));
		}
		if (actions[2]) {
			actions[2] = false;
			giveCard(player, entity, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE_ONE));
		}
		if (actions[3]) {
			actions[3] = false;
			giveCard(player, entity, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE));
		}
		if (actions[4]) {
			actions[4] = false;
			String action = actioneCard(player, card.getFeatures().getFeature(CardFeatureType.ACTION_SPAWN));
			player.getFight().sendMessage(
					player.getFight().getSorci().getMessage().getString((action != null) ? "message_spawn_action" : "message_spawn")
					.replace("{entity}", card.renderName())
					.replace("{action}", action)
			);
		}
		if (actions[5]) {
			actions[5] = false;
			String action = actioneCard(player, card.getFeatures().getFeature(CardFeatureType.ACTION_DEAD));
			player.getFight().sendMessage(
					player.getFight().getSorci().getMessage().getString((action != null) ? "message_dead_action" : "message_dead")
					.replace("{entity}", card.renderName())
					.replace("{action}", action)
			);
		}
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
	
	public boolean nextRound(CardFeatureType type) {
		boolean updateStat = false;
		if (card.getFeatures().hasFeature(type)) {
			CardFeature feature = card.getFeatures().getFeature(type);
			int round = feature.getValue().asRound();
			if (round > 0) {
				feature.setValue(new CardValue(round-1, false));
			} else {
				card.getFeatures().removeFeature(type);
				updateStat = true;
			}
		}
		return updateStat;
	}
	
	public boolean nextRound() {
		boolean updateStat = false;
		if (nextRound(CardFeatureType.INVULNERABILITY))
			updateStat = true;
		if (nextRound(CardFeatureType.IMMOBILIZATION))
			updateStat = true;
		if (nextRound(CardFeatureType.STUNNED))
			updateStat = true;
		return updateStat;
	}
	
	public boolean hasSkin() {
		return skin != null;
	}
	
	public boolean hasIncitement() {
		return this.card.getFeatures().hasFeature(CardFeatureType.INCITEMENT);
	}
	
	public boolean isExited() {
		return this.card.getFeatures().hasFeature(CardFeatureType.EXCITED);
	}
	
	public CardSkin getSkin() {
		return skin;
	}
	
	public Card getCard() {
		return card;
	}
	
}
