package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeature;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardFeatures;
import fr.tangv.sorcicubecore.card.CardMaterial;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.card.CardValue.TypeValue;
import fr.tangv.sorcicubecore.card.CardVisual;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardRender {

	private static String featureToString(SorciCubeSpell sorci, CardFeature feature) {
		CardFeatureType featureType = feature.getType();
		if (featureType.getTypeValue() == TypeValue.UUID) {
			Card card = sorci.getHandlerCards().getCard(feature.getValue().asUUID());
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
			lore.add("§r  "+CardVisual.renderStatCard(card));
		}
		//rarity and faction
		lore.add("");
		lore.add("§r"+sorci.getEnumTool().rarityToString(card.getRarity())+"§r§f§l \u2807 "+sorci.getEnumTool().factionToString(card.getFaction()));
		lore.add("");
		//features
		boolean featureReturn = false;
		for (CardFeatureType type : CardFeatureType.values())
			if (type.isShow() && features.hasFeature(type) && (card.getType() != CardType.ENTITY || type != CardFeatureType.DAMAGE || card.getCible() != CardCible.ONE_ENEMIE || card.getCibleFaction() != CardFaction.BASIC)) {
				CardFeature feature = features.getFeature(type);
				featureReturn = true;
				for (String line : sorci.getEnumTool().featureToString(feature.getType(), feature.getValue())
						.replace("{"+feature.getValue().getType().name().toLowerCase()+"}", featureToString(sorci, feature))
						.replace("{cible}", cible)
						.split("\n")) {
					lore.add("§r"+line);
				}
			}
		if (featureReturn)
			lore.add("");
		//lore
		for (int i = 0; i < card.getDescription().size(); i++)
			lore.add("§r"+card.getDescription().get(i));
		if (card.getDescription().size() >= 1)
			lore.add("");
		//type
		lore.add("§r  "+sorci.getEnumTool().typeToString(card.getType()));
		lore.add("");
		//id
		lore.add("§r§8Id: "+card.getUUID());
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
