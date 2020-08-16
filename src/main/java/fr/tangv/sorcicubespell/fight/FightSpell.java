package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardFeatures;
import fr.tangv.sorcicubespell.util.RenderException;

public class FightSpell {

	private static interface ActionSpell {
		public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads);
	}
	private final static ConcurrentHashMap<CardFeatureType, ActionSpell> actionsSpells;
	
	static {
		actionsSpells = new ConcurrentHashMap<CardFeatureType, ActionSpell>();
		actionsSpells.put(CardFeatureType.SKIN, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.HEALTH, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.DAMAGE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					head.damage(feature.getValue().asInt());
			}
		});
		actionsSpells.put(CardFeatureType.INCITEMENT, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.HIDE_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.TAKE_NEW_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				player.pickCard(feature.getValue().asInt());
			}
		});
		actionsSpells.put(CardFeatureType.DESTRUCT, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					if (head instanceof FightEntity) {
						FightEntity entity = (FightEntity) head;
						if (!entity.isDead())
							entity.dead();
					}
			}
		});
		actionsSpells.put(CardFeatureType.EXCITED, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.APPLY_EXCITED, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					if (head instanceof FightEntity) {
						FightEntity entity = (FightEntity) head;
						if (!entity.isDead())
							entity.setAttackPossible(true);
					}
			}
		});
		actionsSpells.put(CardFeatureType.HEAL, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					head.addHealth(feature.getValue().asInt());
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_DAMAGE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					if (head instanceof FightEntity) {
						FightEntity entity = (FightEntity) head;
						if (!entity.isDead())
							entity.addAttack(feature.getValue().asInt());
					}
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_HEALTH, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					head.addHealth(feature.getValue().asInt());
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_MANA, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight p, CardFeature feature, Collection<FightHead> heads) {
				int mana = feature.getValue().asInt();
				for (FightHead head : heads)
					if (head instanceof FightHero) {
						PlayerFight player = head.getOwner();
						player.addManaBoost(mana);
					}
			}
		});
		actionsSpells.put(CardFeatureType.COPY_CART_ARENA_POSE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				int number = feature.getValue().asInt();
				for (FightHead head : heads)
					if (head instanceof FightEntity) {
						FightEntity ent = (FightEntity) head;
						if (!ent.isDead()) {
							Card card = ent.getCard().getCard();
							int numberPose = 0;
							for (int i = 0; i < player.getMaxEntity() && numberPose < number; i++) {
								FightEntity entity = player.getEntity(i);
								if (!entity.isSelectable()) {
									try {
										entity.setCard(new CardEntity(card));
									} catch (Exception e1) {
										Bukkit.getLogger().warning(RenderException.renderException(e1));
									}
									numberPose++;
								}
							}
							break;
						}
					}
			}
		});
		actionsSpells.put(CardFeatureType.COPY_CART_ARENA, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				for (FightHead head : heads)
					if (head instanceof FightEntity) {
						FightEntity entity = (FightEntity) head;
						if (!entity.isDead()) {
							player.giveCard(entity.getCard().getCard(), feature.getValue().asInt());
							break;
						}
					}
			}
		});
		actionsSpells.put(CardFeatureType.REMOVE_MANA_HERO, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight p, CardFeature feature, Collection<FightHead> heads) {
				int mana = feature.getValue().asInt();
				for (FightHead head : heads)
					if (head instanceof FightHero) {
						PlayerFight player = head.getOwner();
						if (player.canPlay())
							player.removeMana(mana);
						else
							player.removeManaBoost(mana);
					}
			}
		});
		actionsSpells.put(CardFeatureType.INVULNERABILITY, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.IMMOBILIZATION, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.STUNNED, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.IF_ATTACKED_EXEC_ONE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.IF_ATTACKED_EXEC, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.IF_ATTACKED_GIVE_ONE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.IF_ATTACKED_GIVE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.INVOCATION, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					for (int i = 0; i < player.getMaxEntity(); i++) {
						FightEntity entity = player.getEntity(i);
						if (!entity.isSelectable()) {
							try {
								entity.setCard(new CardEntity(cardFeature.clone()));
							} catch (Exception e1) {
								Bukkit.getLogger().warning(RenderException.renderException(e1));
							}
							break;
						}
					}
				}
			}
		});
		actionsSpells.put(CardFeatureType.ACTION_DEAD, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.ACTION_SPAWN, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				//is event
			}
		});
		actionsSpells.put(CardFeatureType.METAMORPH_TO, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					for (FightHead head : heads)
						if (head instanceof FightEntity && head.isSelectable()) {
							FightEntity entity = (FightEntity) head;
							try {
								entity.setCard(new CardEntity(cardFeature));
							} catch (Exception e1) {
								Bukkit.getLogger().warning(RenderException.renderException(e1));
							}
						}
				}
			}
		});
		actionsSpells.put(CardFeatureType.GIVE_FEATURE_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					for (FightHead head : heads)
						if (head instanceof FightEntity) {
							FightEntity entity = (FightEntity) head;
							if (!entity.isDead()) {
								CardFeatures features = entity.getCard().getCard().getFeatures();
								for (CardFeature feat : cardFeature.getFeatures().valueFeatures()) {//<<error give this feature
									if (features.hasFeature(feat.getType()))
										features.removeFeature(feat.getType());
									features.putFeature(feat.clone());
								}
								entity.updateStat();
							}
						}
				}
			}
		});
		actionsSpells.put(CardFeatureType.EXECUTE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					FightSpell.startActionSpell(player, cardFeature.getFeatures(), 
							FightCible.randomFightHeadsForCible(player, cardFeature.getCible(), cardFeature.getCibleFaction()));
				}
			}
		});
	}
	
	public static void startActionSpell(PlayerFight player, CardFeatures features, FightHead head) {
		startActionSpell(player, features, Arrays.asList(head));
	}
	
	public static void startActionSpell(PlayerFight player, CardFeatures features, Collection<FightHead> heads) {
		for (CardFeature feature : features.valueFeatures())
			actionsSpells.get(feature.getType()).actionSpell(player, feature, heads);
	}
	
	public static void startActionFeature(PlayerFight player, CardFeature feature, FightHead head) {
		startActionFeature(player, feature, Arrays.asList(head));
	}
	
	public static void startActionFeature(PlayerFight player, CardFeature feature, Collection<FightHead> heads) {
		actionsSpells.get(feature.getType()).actionSpell(player, feature, heads);
	}
	
}
