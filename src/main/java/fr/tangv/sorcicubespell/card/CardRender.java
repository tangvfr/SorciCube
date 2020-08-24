package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardRender {

	private static String featureToString(SorciCubeSpell sorci, CardFeature feature) {
		CardFeatureType featureType = feature.getType();
		if (featureType == CardFeatureType.INVOCATION ||
				featureType == CardFeatureType.ACTION_DEAD ||
				featureType == CardFeatureType.ACTION_SPAWN ||
				featureType == CardFeatureType.METAMORPH_TO ||
				featureType == CardFeatureType.GIVE_FEATURE_CARD ||
				featureType == CardFeatureType.IF_ATTACKED_EXEC_ONE ||
				featureType == CardFeatureType.IF_ATTACKED_EXEC ||
				featureType == CardFeatureType.IF_ATTACKED_GIVE_ONE ||
				featureType == CardFeatureType.IF_ATTACKED_GIVE ||
				featureType == CardFeatureType.EXECUTE) {
			Card card = sorci.getManagerCards().getCard(feature.getValue().asUUID());
			if (card != null) {
				return card.renderName()+(card.getType() == CardType.ENTITY ? " "+CardVisual.renderStatCard(card) : "");
			} else
				return "nothing";
		} else if ((featureType == CardFeatureType.BOOST_DAMAGE
				|| featureType == CardFeatureType.BOOST_HEALTH
				|| featureType == CardFeatureType.BOOST_MANA
				|| featureType == CardFeatureType.REMOVE_MANA_HERO)
			&& feature.getValue().asNumber() < 0) {
			return Integer.toString(-feature.getValue().asNumber());
		} else
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
		String name = card.renderName()+"§r "+CardVisual.renderManaCard(card);
		ArrayList<String> lore = new ArrayList<String>();
		//damage and health if is entity
		if (card.getType() == CardType.ENTITY) {
			lore.add("  "+CardVisual.renderStatCard(card));
		}
		//rarity and faction
		lore.add("");
		lore.add(sorci.getEnumTool().rarityToString(card.getRarity())+"§r§f§l \u2807 "+sorci.getEnumTool().factionToString(card.getFaction()));
		lore.add("");
		//features
		boolean featureReturn = false;
		for (CardFeatureType type : CardFeatureType.values())
			if (type.isShow() && features.hasFeature(type) && (card.getType() != CardType.ENTITY || type != CardFeatureType.DAMAGE || card.getCible() != CardCible.ONE_ENEMIE || card.getCibleFaction() != CardFaction.BASIC)) {
				CardFeature feature = features.getFeature(type);
				featureReturn = true;
				lore.add(sorci.getEnumTool().featureToString(feature.getType(), feature.getValue())
					.replace("{"+feature.getValue().getType().name().toLowerCase()+"}", featureToString(sorci, feature))
					.replace("{cible}", cible)
				);
			}
		if (featureReturn)
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
		if (material.hasSkin())
			return ItemBuild.buildSkull(material.getSkin().getTexture(),
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
