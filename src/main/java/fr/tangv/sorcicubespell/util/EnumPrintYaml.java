package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;

import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardComparator;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;
import fr.tangv.sorcicubespell.card.CardValue.TypeValue;

public class EnumPrintYaml {

	public static void main(String[] args) {
		list(CardCible.ALL, "cible");
		list(CardFaction.DARK, "faction");
		list(CardRarity.COMMUN, "rarity");
		list(CardType.ENTITY, "type");
		list(CardComparator.BY_ID, "sort");
		try {
			listCardFeatureType(CardFeatureType.ACTION_DEAD, "feature");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
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
	
	public static void listCardFeatureType(CardFeatureType enum1, String name) throws IllegalAccessException {
		System.out.println(name+":");
		for (Field field : enum1.getClass().getFields()) {
			try {
				String named = field.getName().toLowerCase();
				CardFeatureType type = (CardFeatureType) field.get(enum1);
				String value = type.getTypeValue() == TypeValue.NONE ? "" : " {"+type.getTypeValue().name().toLowerCase()+"}";
				System.out.println("  "+named+": \""+named.toUpperCase().replace("_", " ")+value+"\"");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}
	
}
