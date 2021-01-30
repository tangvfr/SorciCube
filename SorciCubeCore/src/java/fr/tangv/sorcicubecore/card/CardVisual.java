package fr.tangv.sorcicubecore.card;

public class CardVisual {

	public static String renderStatCard(Card card) {
		CardFeatures features = card.getFeatures();
		String string = "";
		if (features.hasFeature(CardFeatureType.DAMAGE))
			string += "§e"+features.getFeature(CardFeatureType.DAMAGE).getValue().toString()+" \u2694";
		if (features.hasFeature(CardFeatureType.HEALTH))
			string += "  §c"+features.getFeature(CardFeatureType.HEALTH).getValue().toString()+" \u2665";
		if (features.hasFeature(CardFeatureType.INCITEMENT))
			string += "  §7\u2617";
		if (features.hasFeature(CardFeatureType.IMMOBILIZATION))
			string += "  §b\u2744";
		if (features.hasFeature(CardFeatureType.STUNNED))
			string += "  §6\u0040";
		if (features.hasFeature(CardFeatureType.INVULNERABILITY))
			string += "  §d\u267e";
		return string;
	}
	
	public static String renderManaCard(Card card) {
		return "§b"+card.getMana()+" \u2756";
	}
	
}
