package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFeature;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardFeatures;

public class FightSpell {

	private static interface ActionSpell {
		public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head);
	}
	private final static ConcurrentHashMap<CardFeatureType, ActionSpell> actionsSpells;
	
	static {
		actionsSpells = new ConcurrentHashMap<CardFeatureType, ActionSpell>();
		actionsSpells.put(CardFeatureType.SKIN, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.HEALTH, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.DAMAGE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.INCITEMENT, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.HIDE_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.TAKE_NEW_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.DESTRUCT, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.HEAL, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_DAMAGE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_HEALTH, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.BOOST_MANA, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.COPY_CART_ARENA_POSE, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.COPY_CART_ARENA, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.REMOVE_MANA_HERO, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.INVULNERABILITY, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				//nothing
			}
		});
		actionsSpells.put(CardFeatureType.IMMOBILIZATION, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				
			}
		});
		actionsSpells.put(CardFeatureType.IF_HURT, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
		actionsSpells.put(CardFeatureType.INVOCATION, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
		actionsSpells.put(CardFeatureType.ACTION_DEAD, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
		actionsSpells.put(CardFeatureType.ACTION_SPAWN, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
		actionsSpells.put(CardFeatureType.METAMORPH_TO, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
		actionsSpells.put(CardFeatureType.GIVE_FEATURE_CART, new ActionSpell() {
			@Override
			public void actionSpell(PlayerFight player, CardFeature feature, Collection<FightHead> head) {
				Card cardFeature = player.getFight().getSorci().getManagerCards().getCard(UUID.fromString(feature.getValue().asString()));
				if (cardFeature != null) {
					
				}
			}
		});
	}
	
	public static void startActionSpell(PlayerFight player, CardFeatures features, Collection<FightHead> head) {
		for (CardFeature feature : features.valueFeatures())
			actionsSpells.get(feature.getType()).actionSpell(player, feature, head);
	}
	
	public static void startActionSpell(PlayerFight player, CardFeatures features, FightHead head) {
		startActionSpell(player, features, Arrays.asList(head));
	}
	
}
