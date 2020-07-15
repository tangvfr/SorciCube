package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardRender {

	private static String featureToString(SorciCubeSpell sorci, Map<UUID, Card> cards, CardFeature feature) {
		if (feature.getType() == CardFeatureType.INVOCATION ||
				feature.getType() == CardFeatureType.ACTION_DEAD ||
				feature.getType() == CardFeatureType.ACTION_SPAWN ||
				feature.getType() == CardFeatureType.METAMORPH_TO ||
				feature.getType() == CardFeatureType.GIVE_FEATURE_CART) {
			UUID uuid = UUID.fromString(feature.getValue().asString());
			if (cards.containsKey(uuid)) {
				Card card = cards.get(uuid);
				return card.getName()+(card.getType() == CardType.ENTITY ? " "+renderStatCard(card) : "");
			} else
				return "nothing";
		} else
			return feature.getValue().toString();
	}
	
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci, Map<UUID, Card> cards) {
		return CardRender.cardToItem(card, sorci, cards, 1, false);
	}
	
	public static String renderStatCard(Card card) {
		CardFeatures features = card.getFeatures();
		return "§e"+features.getFeature(CardFeatureType.DAMAGE).getValue().toString()+" \u2694"
				+"  §c"+features.getFeature(CardFeatureType.HEALTH).getValue().toString()+" \u2665";
	}
	
	public static String renderManaCard(Card card) {
		return "§b"+card.getMana()+" \u2756";
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci, Map<UUID, Card> cards, int amount, boolean ench) {
		if (card == null) return null;
		CardFeatures features = card.getFeatures();
		String cible = sorci.getEnumTool().cibleToString(card.getCible())+
				(card.getCibleFaction() != CardFaction.BASIC ? (" "+sorci.getEnumTool().factionToString(card.getCibleFaction())) : "");
		String name = card.getName()+"§r "+renderManaCard(card);
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
		boolean featureReturn = false;
		for (CardFeature feature : features.valueFeatures())
			if (feature.getType().isShow())
				if (card.getType() != CardType.ENTITY || feature.getType() != CardFeatureType.DAMAGE || card.getCible() != CardCible.ONE_ENEMIE || card.getCibleFaction() != CardFaction.BASIC) {
					featureReturn = true;
					lore.add(sorci.getEnumTool().featureToString(feature.getType())
						.replace("{"+feature.getValue().getType().name().toLowerCase()+"}", featureToString(sorci, cards, feature))
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
