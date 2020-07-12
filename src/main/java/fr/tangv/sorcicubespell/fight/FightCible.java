package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.tangv.sorcicubespell.card.CardCible;

public enum FightCible {

	HERO_ALLY(),
	ENTITY_1_ALLY(),
	ENTITY_2_ALLY(),
	ENTITY_3_ALLY(),
	ENTITY_4_ALLY(),
	ENTITY_5_ALLY(),	
	HERO_ENEMIE(),
	ENTITY_1_ENEMIE(),
	ENTITY_2_ENEMIE(),
	ENTITY_3_ENEMIE(),
	ENTITY_4_ENEMIE(),
	ENTITY_5_ENEMIE();
	
	private static ConcurrentHashMap<CardCible, Vector<FightCible>> lists;
	
	static {
		lists = new ConcurrentHashMap<CardCible, Vector<FightCible>>();
		Vector<FightCible> allEntityAlly = new Vector<FightCible>(Arrays.asList(
				FightCible.ENTITY_1_ALLY,
				FightCible.ENTITY_2_ALLY,
				FightCible.ENTITY_3_ALLY,
				FightCible.ENTITY_4_ALLY,
				FightCible.ENTITY_5_ALLY
			));
		Vector<FightCible> allEntityEnemie = new Vector<FightCible>(Arrays.asList(
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
	
}
