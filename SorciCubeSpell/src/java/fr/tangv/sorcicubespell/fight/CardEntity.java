package fr.tangv.sorcicubespell.fight;

import java.util.Vector;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardFeature;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardFeatures;
import fr.tangv.sorcicubecore.card.CardSkin;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue;
import fr.tangv.sorcicubecore.configs.ActionMessageConfig;

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
	
	private void sendMessageAction(PlayerFight owner, String nameEntity, ActionMessageConfig message, String action) {
		owner.fight.sendMessage(
			(action == null) ?
				message.withoutAction.value
					.replace("{entity}", nameEntity)
					.replace("{owner}", owner.getNamePlayer())
			:
				message.withAction.value
					.replace("{entity}", nameEntity)
					.replace("{action}", action)
					.replace("{owner}", owner.getNamePlayer())
		);
	}
	
	private void actionCard(PlayerFight owner, String nameEntity, CardFeature feature, ActionMessageConfig message) {
		Card card = owner.fight.getSorci().getHandlerCards().get(feature.getValue().asUUID());
		if (card != null) {
			Vector<FightHead> heads = FightCibles.randomFightHeadsForCible(owner, card.getCible(), card.getCibleFaction());
			if (message != null) {
				if(heads.isEmpty())
					sendMessageAction(owner, nameEntity, message, null);
				else
					sendMessageAction(owner, nameEntity, message, card.renderName());
			}
			FightSpell.startActionSpell(owner, card.getFeatures(), heads);
		} else if (message!= null) {
			sendMessageAction(owner, nameEntity, message, "nothing");
		}
	}
	
	private void giveCard(PlayerFight owner ,String nameEntity, FightEntity entity, CardFeature feature, ActionMessageConfig message) {
		Card card = owner.fight.getSorci().getHandlerCards().get(feature.getValue().asUUID());
		if (card != null) {
			sendMessageAction(owner, nameEntity, message, card.renderName());
			FightSpell.startActionFeature(owner, new CardFeature(CardFeatureType.GIVE_FEATURE_CARD, feature.getValue()), entity);
		} else if (message != null) {
			sendMessageAction(owner, nameEntity, message, "nothing");
		}
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
		//Bukkit.broadcastMessage("§e[§aDebug§e] "+entity.getNameInChat()+" "+Arrays.toString(actions));
		if (actions[0]) {
			actions[0] = false;
			actionCard(player, card.renderName(), card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC_ONE), player.fight.config.messages.isAttackOne);
		}
		if (actions[1] && !actions[5]) {
			actions[1] = false;
			actionCard(player, card.renderName(), card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_EXEC), player.fight.config.messages.isAttack);
		}
		if (actions[2]) {
			actions[2] = false;
			giveCard(player, card.renderName(), entity, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE_ONE), player.fight.config.messages.isAttackGiveOne);
		}
		if (actions[3] && !actions[5]) {
			actions[3] = false;
			giveCard(player, card.renderName(), entity, card.getFeatures().getFeature(CardFeatureType.IF_ATTACKED_GIVE), player.fight.config.messages.isAttackGive);
		}
		if (actions[4]) {
			actions[4] = false;
			actionCard(player, card.renderName(), card.getFeatures().getFeature(CardFeatureType.ACTION_SPAWN), player.fight.config.messages.spawn);
		}
		if (actions[5]) {
			actions[5] = false;
			actionCard(player, card.renderName(), card.getFeatures().getFeature(CardFeatureType.ACTION_DEAD), player.fight.config.messages.dead);
			actions[1] = false;
			actions[3] = false;
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
