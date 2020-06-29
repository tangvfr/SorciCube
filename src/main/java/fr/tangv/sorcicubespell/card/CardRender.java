package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardRender {

	private static String featureToString(SorciCubeSpell sorci, CardFeature feature) {
		return feature.getValue().toString();
	}
	
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci) {
		return CardRender.cardToItem(card, sorci, 1, false);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci, int amount, boolean ench) {
		if (card == null) return null;
		CardFeatures features = card.getFeatures();
		String cible = sorci.getEnumTool().cibleToString(card.getCible())+
				(card.getCibleFaction() != CardFaction.BASIC ? (" "+sorci.getEnumTool().factionToString(card.getCibleFaction())) : "");
		String name = card.getName()+"§r §b2\u2756";
		ArrayList<String> lore = new ArrayList<String>();
		//rarity and faction
		lore.add(sorci.getEnumTool().factionToString(card.getFaction()));
		lore.add(sorci.getEnumTool().rarityToString(card.getRarity()));
		//damage and health if is entity
		if (card.getType() == CardType.ENTITY) {
			lore.add("  §e"+features.getFeature(CardFeatures.ATTACK_DAMMAGE).getValue().toString()+" \u2694"
					+" §c"+features.getFeature(CardFeatures.HEALTH).getValue().toString()+" \u2665");
		}
		lore.add("");
		//features
		for (CardFeature feature : features.listFeatures())
			if (feature.getType() != CardFeatureType.SKIN && feature.getType() != CardFeatureType.HEALTH)
				lore.add(sorci.getEnumTool().featureToString(feature.getType())
						.replace("{"+feature.getValue().getType().name().toLowerCase()+"}", featureToString(sorci, feature))
						.replace("{cible}", cible)
					);
		//lore
		lore.add("");
		for (int i = 0; i < card.getDescription().size(); i++)
			lore.add(card.getDescription().get(i));
		//type
		lore.add("");
		lore.add(sorci.getEnumTool().typeToString(card.getType()));
		lore.add("");
		//id
		lore.add("§8Id: "+card.getUUID());
		//return item
		CardMaterial material = card.getMaterial();
		if (material.hasUrl())
			return ItemBuild.buildSkull(material.getUrl(),
					amount,
					name,
					lore,
					ench);
		else
			return ItemBuild.buildItem(Material.getMaterial(material.getId()),
					amount,
					(short) 0,
					(byte) material.getData(),
					name,
					lore,
					ench);
	}
	
}
