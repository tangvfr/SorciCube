package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.fight.FightCible;

public class FightCibles {

	//static lists
	
	private static final ConcurrentHashMap<CardCible, Vector<FightCible>> lists;
	private static final Vector<FightCible> allEntityAlly;
	private static final Vector<FightCible> allEntityEnemie;
	
	static {
		lists = new ConcurrentHashMap<CardCible, Vector<FightCible>>();
		allEntityAlly = new Vector<FightCible>(Arrays.asList(
				FightCible.ENTITY_1_ALLY,
				FightCible.ENTITY_2_ALLY,
				FightCible.ENTITY_3_ALLY,
				FightCible.ENTITY_4_ALLY,
				FightCible.ENTITY_5_ALLY
			));
		allEntityEnemie = new Vector<FightCible>(Arrays.asList(
				FightCible.ENTITY_1_ENEMIE,
				FightCible.ENTITY_2_ENEMIE,
				FightCible.ENTITY_3_ENEMIE,
				FightCible.ENTITY_4_ENEMIE,
				FightCible.ENTITY_5_ENEMIE
			));
		lists.put(CardCible.NONE, new Vector<FightCible>());
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityEnemie);
			list.addAll(allEntityAlly);
			lists.put(CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.add(FightCible.HERO_ENEMIE);
			lists.put(CardCible.ONE_HERO, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.add(FightCible.HERO_ENEMIE);
			lists.put(CardCible.ALL_HERO, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.add(FightCible.HERO_ENEMIE);
			list.addAll(allEntityAlly);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ONE, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ENEMIE);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ONE_ENEMIE, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ONE_ENTITY_ENEMIE, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ENEMIE);
			lists.put(CardCible.HERO_ENEMIE, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ENEMIE);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ALL_ENEMIE, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ALL_ENTITY_ENEMIE, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.addAll(allEntityAlly);
			lists.put(CardCible.ONE_ALLY, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityAlly);
			lists.put(CardCible.ONE_ENTITY_ALLY, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			lists.put(CardCible.HERO_ALLY, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.addAll(allEntityAlly);
			lists.put(CardCible.ALL_ALLY, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityAlly);
			lists.put(CardCible.ALL_ENTITY_ALLY, list); 
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityAlly);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ONE_ENTITY, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.addAll(allEntityAlly);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ALL_ENTITY, list);
		}
		{
			Vector<FightCible> list = new Vector<FightCible>();
			list.add(FightCible.HERO_ALLY);
			list.add(FightCible.HERO_ENEMIE);
			list.addAll(allEntityAlly);
			list.addAll(allEntityEnemie);
			lists.put(CardCible.ALL, list);
		}
	}
	
	public static Vector<FightCible> listForCardCible(CardCible cible) {
		return lists.get(cible);
	}
	
	private static Vector<FightHead> listForCibles(PlayerFight player, Vector<FightCible> cibles, CardFaction faction) {
		Vector<FightHead> list = new Vector<FightHead>();
		for (FightHead head : player.getForCibles(cibles))
			if (head.isFaction(faction))
				list.add(head);
		return list;
	}
	
	private static void addRandomInList(Vector<FightHead> listIn, Vector<FightHead> listOut) {
		if (listIn.size() > 0)
			listOut.add(listIn.get((int) (Math.random()*listIn.size())));
	}
	
	public static Vector<FightHead> randomFightHeadsForCible(PlayerFight player, CardCible cible, CardFaction faction) {
		if (cible == CardCible.NONE)
			return new Vector<FightHead>();
		Vector<FightHead> list = listForCibles(player, lists.get(cible), faction);
		if (cible.hasChoose() && list.size() > 0) {
			Vector<FightHead> cibles = new Vector<FightHead>();
			if (cible == CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE) {
				addRandomInList(listForCibles(player, allEntityAlly, faction), cibles);
				addRandomInList(listForCibles(player, allEntityEnemie, faction), cibles);
			} else {
				addRandomInList(list, cibles);
			}
			return cibles;
		} else
			return list;
	}
	
}
