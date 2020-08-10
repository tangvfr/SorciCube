package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardFaction;

public enum FightCible {

	HERO_ALLY(true, true),
	ENTITY_1_ALLY(false, true),
	ENTITY_2_ALLY(false, true),
	ENTITY_3_ALLY(false, true),
	ENTITY_4_ALLY(false, true),
	ENTITY_5_ALLY(false, true),	
	HERO_ENEMIE(true, false),
	ENTITY_1_ENEMIE(false, false),
	ENTITY_2_ENEMIE(false, false),
	ENTITY_3_ENEMIE(false, false),
	ENTITY_4_ENEMIE(false, false),
	ENTITY_5_ENEMIE(false, false);
	
	private boolean ally;
	private boolean hero;
	
	private FightCible(boolean hero, boolean ally) {
		this.hero = hero;
		this.ally = ally;
	}
	
	public boolean isHero() {
		return hero;
	}
	
	public boolean isAlly() {
		return ally;
	}
	
	//static lists
	
	private static ConcurrentHashMap<CardCible, Vector<FightCible>> lists;
	private static Vector<FightCible> allEntityAlly;
	private static Vector<FightCible> allEntityEnemie;
	
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
				FightCible.ENTITY_1_ALLY,
				FightCible.ENTITY_2_ALLY,
				FightCible.ENTITY_3_ALLY,
				FightCible.ENTITY_4_ALLY,
				FightCible.ENTITY_5_ALLY
			));
		Vector<FightCible> list;
		//------------------------------------
		list = new Vector<FightCible>();
		lists.put(CardCible.NONE, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityAlly);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addElement(FightCible.HERO_ENEMIE);
		lists.put(CardCible.ONE_HERO, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addElement(FightCible.HERO_ENEMIE);
		lists.put(CardCible.ALL_HERO, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addElement(FightCible.HERO_ENEMIE);
		list.addAll(allEntityAlly);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ONE, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ENEMIE);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ONE_ENEMIE, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ONE_ENTITY_ENEMIE, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ENEMIE);
		lists.put(CardCible.HERO_ENEMIE, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ENEMIE);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ALL_ENEMIE, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ALL_ENTITY_ENEMIE, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addAll(allEntityAlly);
		lists.put(CardCible.ONE_ALLY, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityAlly);
		lists.put(CardCible.ONE_ENTITY_ALLY, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		lists.put(CardCible.HERO_ALLY, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addAll(allEntityAlly);
		lists.put(CardCible.ALL_ALLY, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityAlly);
		lists.put(CardCible.ALL_ENTITY_ALLY, list); 
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityAlly);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ONE_ENTITY, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addAll(allEntityAlly);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ALL_ENTITY, list);
		//------------------------------------
		list = new Vector<FightCible>();
		list.addElement(FightCible.HERO_ALLY);
		list.addElement(FightCible.HERO_ENEMIE);
		list.addAll(allEntityAlly);
		list.addAll(allEntityEnemie);
		lists.put(CardCible.ALL, list);
		//------------------------------------
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
