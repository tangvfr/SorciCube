package fr.tangv.sorcicubespell.util;

import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardComparator;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue;
import fr.tangv.sorcicubecore.configs.CibleEnumConfig;
import fr.tangv.sorcicubecore.configs.FactionEnumConfig;
import fr.tangv.sorcicubecore.configs.FeatureEnumConfig;
import fr.tangv.sorcicubecore.configs.RarityEnumConfig;
import fr.tangv.sorcicubecore.configs.SortEnumConfig;
import fr.tangv.sorcicubecore.configs.TypeEnumConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class EnumTool {

	private final SorciCubeSpell sorci;
	
	public EnumTool(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	public String cibleToString(CardCible cible) {
		CibleEnumConfig c = sorci.config().enums.cible;
		switch (cible) {
			case ALL:
				return c.all.value;
				
			case ALL_ALLY:
				return c.allAlly.value;
				
			case ALL_ENEMIE:
				return c.allEnemie.value;
				
			case ALL_ENTITY:
				return c.allEntity.value;
				
			case ALL_ENTITY_ALLY:
				return c.allEntityAlly.value;
				
			case ALL_ENTITY_ENEMIE:
				return c.allEntityEnemie.value;
				
			case ALL_HERO:
				return c.allHero.value;
				
			case HERO_ALLY:
				return c.heroAlly.value;
				
			case HERO_ENEMIE:
				return c.allEntityEnemie.value;
				
			case NONE:
				return c.none.value;
				
			case ONE:
				return c.one.value;
				
			case ONE_ALLY:
				return c.oneAlly.value;
				
			case ONE_ENEMIE:
				return c.oneEnemie.value;
				
			case ONE_ENTITY:
				return c.oneEntity.value;
				
			case ONE_ENTITY_ALLY:
				return c.oneEntityAlly.value;
				
			case ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE:
				return c.oneEntityAllyAndOneEntityEnemie.value;
				
			case ONE_ENTITY_ENEMIE:
				return c.oneEntityEnemie.value;
				
			case ONE_HERO:
				return c.oneHero.value;
				
			default:
				return null;
		}
	}
	
	public String factionToString(CardFaction faction) {
		FactionEnumConfig f = sorci.config().enums.faction;
		switch (faction) {
			case BASIC:
				return CardFaction.BASIC.getColor()+f.basic.value;
				
			case DARK:
				return CardFaction.DARK.getColor()+f.dark.value;
				
			case LIGHT:
				return CardFaction.LIGHT.getColor()+f.light.value;
				
			case NATURE:
				return CardFaction.NATURE.getColor()+f.nature.value;
				
			case TOXIC:
				return CardFaction.TOXIC.getColor()+f.toxic.value;
				
			default:
				return null;
		}
	}
	
	public String rarityToString(CardRarity rarity) {
		RarityEnumConfig r = sorci.config().enums.rarity;
		switch (rarity) {
			case COMMUN:
				return CardRarity.COMMUN.getColor()+r.common.value;
				
			case RARE:
				return  CardRarity.RARE.getColor()+r.rare.value;
				
			case EPIC:
				return  CardRarity.EPIC.getColor()+r.epic.value;
						
			case LEGENDARY:
				return  CardRarity.LEGENDARY.getColor()+r.legendary.value;
						
			default:
				return null;
		}
	}
	
	public String typeToString(CardType type) {
		TypeEnumConfig t = sorci.config().enums.type;
		switch (type) {
			case ENTITY:
				 return t.entity.value;
				
			case SPELL:
				return t.spell.value;
				
			default:
				return null;
		}
	}
	
	public String sortToString(CardComparator sort) {
		SortEnumConfig s = sorci.config().enums.sort;
		switch (sort) {
			case BY_FACTION:
				return s.byFaction.value;
				
			case BY_HIGH_MANA:
				return s.byHighMana.value;
				
			case BY_ID:
				return s.byId.value;
				
			case BY_LOW_MANA:
				return s.byLowMana.value;
				
			case BY_NAME:
				return s.byName.value;
				
			case BY_RARITY:
				return s.byRarity.value;
				
			case BY_TYPE:
				return s.byType.value;
				
			default:
				return null;
		}
	}
	
	public String featureToString(CardFeatureType featureType, CardValue value) {
		FeatureEnumConfig f = sorci.config().enums.feature;
		switch (featureType) {
			case ACTION_DEAD:
				return f.actionDead.value;
				
			case ACTION_SPAWN:
				return f.actionSpawn.value;
						
			case APPLY_EXCITED:
				return f.applyExcited.value;
				
			case BOOST_DAMAGE:
				return value.asNumber() < 0 ? f.boostDamageNeg.value : f.boostDamage.value;
						
			case BOOST_HEALTH:
				return value.asNumber() < 0 ? f.boostHealthNeg.value : f.boostHealth.value;
						
			case BOOST_MANA:
				return value.asNumber() < 0 ? f.boostManaNeg.value : f.boostMana.value;
						
			case COPY_CARD_ARENA:
				return f.copyCardArena.value;
						
			case COPY_CARD_ARENA_POSE:
				return f.copyCardArenaPose.value;
						
			case DAMAGE:
				return f.damage.value;
						
			case DESTRUCT:
				return f.destruct.value;
						
			case EXCITED:
				return f.excited.value;
						
			case EXECUTE:
				return f.execute.value;
						
			case GIVE_CARD:
				return f.giveCard.value;
						
			case GIVE_FEATURE_CARD:
				return f.giveFeatureCard.value;
						
			case HEAL:
				return f.heal.value;
						
			case HEALTH:
				return f.health.value;
						
			case HIDE_CARD:
				return f.hideCard.value;
						
			case IF_ATTACKED_EXEC:
				return f.ifAttackedExec.value;
						
			case IF_ATTACKED_EXEC_ONE:
				return f.ifAttackedExecOne.value;
						
			case IF_ATTACKED_GIVE:
				return f.ifAttackedGive.value;
						
			case IF_ATTACKED_GIVE_ONE:
				return f.ifAttackedGiveOne.value;
						
			case IMMOBILIZATION:
				return f.immobilization.value;
						
			case INCITEMENT:
				return f.incitement.value;
						
			case INVOCATION:
				return f.invocation.value;
						
			case INVULNERABILITY:
				return f.invulnerability.value;
						
			case METAMORPH_TO:
				return f.metamorphTo.value;
						
			case REMOVE_CARD:
				return f.removeCard.value;
						
			case REMOVE_MANA_HERO:
				return value.asNumber() < 0 ? f.removeManaHeroNeg.value : f.removeManaHero.value;
						
			case SKIN:
				return f.skin.value;
						
			case STUNNED:
				return f.stunned.value;
						
			case TAKE_NEW_CARD:
				return f.takeNewCard.value;
						
			default:
				return null;
		}
	}
	
}
