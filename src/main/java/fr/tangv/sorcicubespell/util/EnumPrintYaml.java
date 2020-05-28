package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;

import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartFaction;
import fr.tangv.sorcicubespell.carts.CartRarity;
import fr.tangv.sorcicubespell.carts.CartType;

public class EnumPrintYaml {

	public static void main(String[] args) {
		list(CartCible.ALL, "cible");
		list(CartFaction.DARK, "faction");
		list(CartRarity.COMMUN, "rarity");
		list(CartType.ENTITY, "type");
	}
	
	public static void list(Enum<?> enum1, String name) {
		System.out.println(name+":");
		for (Field field : enum1.getClass().getFields()) {
			try {
				String named = ((Enum<?>) field.get(enum1)).name().toLowerCase();
				System.out.println("  "+named+": \""+named.toUpperCase().replace("_", " ")+"\"");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
