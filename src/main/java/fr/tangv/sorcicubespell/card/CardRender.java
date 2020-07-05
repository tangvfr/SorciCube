package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;
import java.util.Collection;

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
	
	public static String renderStatCard(Card card) {
		CardFeatures features = card.getFeatures();
		return "§e"+features.getFeature(CardFeatures.ATTACK_DAMMAGE).getValue().toString()+" \u2694"
				+"  §c"+features.getFeature(CardFeatures.HEALTH).getValue().toString()+" \u2665";
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci, int amount, boolean ench) {
		if (card == null) return null;
		CardFeatures features = card.getFeatures();
		String cible = sorci.getEnumTool().cibleToString(card.getCible())+
				(card.getCibleFaction() != CardFaction.BASIC ? (" "+sorci.getEnumTool().factionToString(card.getCibleFaction())) : "");
		String name = card.getName()+"§r §b"+card.getMana()+" \u2756";
		ArrayList<String> lore = new ArrayList<String>();
		//damage and health if is entity
		if (card.getType() == CardType.ENTITY) {
			lore.add("  "+renderStatCard(card));
		}
		//rarity and faction
		lore.add("");
		lore.add(sorci.getEnumTool().rarityToString(card.getRarity())+"§r§f§l \u2807 "+sorci.getEnumTool().factionToString(card.getFaction()));
		/*lore.add(sorci.getEnumTool().factionToString(card.getFaction()));
		lore.add(sorci.getEnumTool().rarityToString(card.getRarity()));*/
		lore.add("");
		//features
		boolean rn = false;
		Collection<CardFeature> f = features.listFeatures();
		for (CardFeature feature : f)
			if (feature.getType() != CardFeatureType.SKIN && feature.getType() != CardFeatureType.HEALTH) {
				if (!(card.getType() == CardType.ENTITY && feature.getName().equals(CardFeatures.ATTACK_DAMMAGE) && card.getCible() == CardCible.ONE_ENEMIE && card.getCibleFaction() == CardFaction.BASIC)) {
					rn = true;
					lore.add(sorci.getEnumTool().featureToString(feature.getType())
						.replace("{"+feature.getValue().getType().name().toLowerCase()+"}", featureToString(sorci, feature))
						.replace("{cible}", cible)
					);
				}
			}
		if (rn)
			lore.add("");
		//lore
		for (int i = 0; i < card.getDescription().size(); i++)
			lore.add(card.getDescription().get(i));
		if (card.getDescription().size() >= 1)
			lore.add("");
		//type
		lore.add("  "+sorci.getEnumTool().typeToString(card.getType()));
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
