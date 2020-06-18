package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;

import fr.tangv.sorcicubespell.cards.CardCible;
import fr.tangv.sorcicubespell.cards.CardComparator;
import fr.tangv.sorcicubespell.cards.CardFaction;
import fr.tangv.sorcicubespell.cards.CardRarity;
import fr.tangv.sorcicubespell.cards.CardType;

public class EnumPrintYaml {

	public static void main(String[] args) {
		list(CardCible.ALL, "cible");
		list(CardFaction.DARK, "faction");
		list(CardRarity.COMMUN, "rarity");
		list(CardType.ENTITY, "type");
		list(CardComparator.BY_ID, "sort");
	}
	
	public static void list(Enum<?> enum1, String name) {
		System.out.println(name+":");
		for (Field field : enum1.getClass().getFields()) {
			try {
				String named = field.getName().toLowerCase();
				System.out.println("  "+named+": \""+named.toUpperCase().replace("_", " ")+"\"");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}
	
}
