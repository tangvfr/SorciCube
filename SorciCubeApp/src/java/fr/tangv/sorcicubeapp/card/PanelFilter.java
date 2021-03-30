package fr.tangv.sorcicubeapp.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.tangv.sorcicubecore.card.Card;
import fr.tangv.sorcicubecore.card.CardCible;
import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardRarity;
import fr.tangv.sorcicubecore.card.CardType;
import fr.tangv.sorcicubecore.handler.HandlerCards;

public class PanelFilter extends JScrollPane {

	private static final long serialVersionUID = -6789820987468634151L;
	private PanelFilterEnum<CardType> filterType;
	private PanelFilterEnum<CardRarity> filterRarity;
	private PanelFilterEnum<CardFaction> filterFaction;
	private PanelFilterEnum<CardFaction> filterCibleFaction;
	private PanelFilterEnum<CardCible> filterCible;
	private PanelFilterBoolean filterOriginalName;
	private PanelFilterBoolean filterHideCard;
	private PanelFilterBoolean filterHasSkin;
	private PanelFilterBoolean filterHasIncitement;
	private PanelFilterBoolean filterIsExited;
	private PanelFilterBoolean filterIsInvulnerability;
	private PanelFilterBoolean filterIsImmobilization;
	private PanelFilterBoolean filterIsStunned;
	private PanelFilterBoolean filterHasNUUID;
	
	public static final class PanelFilterException extends Exception {
		
		private static final long serialVersionUID = 3015619588366530847L;

		private PanelFilterException(String message) {
			super(message);
		}
		
	}
	
	public PanelFilter() throws PanelFilterException {
		//init value
		try {
			this.filterType = new PanelFilterEnum<CardType>(CardType.values(), "Type", BoxLayout.X_AXIS, false);
			this.filterRarity = new PanelFilterEnum<CardRarity>(CardRarity.values(), "Rarity", BoxLayout.Y_AXIS, false);
			this.filterFaction = new PanelFilterEnum<CardFaction>(CardFaction.values(), "Faction", BoxLayout.Y_AXIS, false);
			this.filterCibleFaction = new PanelFilterEnum<CardFaction>(CardFaction.values(), "Cible Faction", BoxLayout.Y_AXIS, false);
			this.filterCible = new PanelFilterEnum<CardCible>(CardCible.values(), "Cible", 0, true);
		} catch (Exception e) {
			throw new PanelFilterException("Error with FilterEnum");
		}
		this.filterOriginalName = new PanelFilterBoolean("Orignal Name", "True", "False", "Any");
		this.filterHideCard = new PanelFilterBoolean("Hide Card", "True", "False", "Any");
		this.filterHasSkin = new PanelFilterBoolean("Has Skin", "True", "False", "Any");
		this.filterHasIncitement = new PanelFilterBoolean("Has Incitement", "True", "False", "Any");
		this.filterIsExited = new PanelFilterBoolean("Is Exited", "True", "False", "Any");
		this.filterIsInvulnerability = new PanelFilterBoolean("Is Invulnerability", "True", "False", "Any");
		this.filterIsImmobilization = new PanelFilterBoolean("Is Immobilization", "True", "False", "Any");
		this.filterIsStunned = new PanelFilterBoolean("Is Stunned", "True", "False", "Any");
		this.filterHasNUUID = new PanelFilterBoolean("Has None UUID", "True", "False", "Any");
		//init gui
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.add(filterType);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(filterRarity);
		panel1.add(filterFaction);
		panel1.add(filterCibleFaction);
		pan.add(panel1);
		pan.add(filterOriginalName);
		pan.add(filterHideCard);
		pan.add(filterHasSkin);
		pan.add(filterHasIncitement);
		pan.add(filterIsExited);
		pan.add(filterIsInvulnerability);
		pan.add(filterIsImmobilization);
		pan.add(filterIsStunned);
		pan.add(filterHasNUUID);
		pan.add(filterCible);
		//scroll pan
		this.setViewportView(pan);
		this.getVerticalScrollBar().setUnitIncrement(10);
	}
	
	public Vector<Card> applyFilter(Collection<Card> list, HandlerCards handler) {
		ArrayList<CardType> filterType = this.filterType.makeFilter();
		ArrayList<CardRarity> filterRarity = this.filterRarity.makeFilter();
		ArrayList<CardFaction> filterFaction = this.filterFaction.makeFilter();
		ArrayList<CardFaction> filterCibleFaction = this.filterCibleFaction.makeFilter();
		ArrayList<CardCible> filterCible = this.filterCible.makeFilter();
		Vector<Card> cards = new Vector<Card>();
		for (Card card : list) {
			if (filterType.contains(card.getType())
				&& filterRarity.contains(card.getRarity())
				&& filterFaction.contains(card.getFaction())
				&& filterCibleFaction.contains(card.getCibleFaction())
				&& filterCible.contains(card.getCible())
				&& filterOriginalName.isGood(card.isOriginalName())
				&& filterHideCard.isGood(card.getFeatures().hasFeature(CardFeatureType.HIDE_CARD))
				&& filterHasSkin.isGood(card.getFeatures().hasFeature(CardFeatureType.SKIN))
				&& filterHasIncitement.isGood(card.getFeatures().hasFeature(CardFeatureType.INCITEMENT))
				&& filterIsExited.isGood(card.getFeatures().hasFeature(CardFeatureType.EXCITED))
				&& filterIsInvulnerability.isGood(card.getFeatures().hasFeature(CardFeatureType.INVULNERABILITY))
				&& filterIsImmobilization.isGood(card.getFeatures().hasFeature(CardFeatureType.IMMOBILIZATION))
				&& filterIsStunned.isGood(card.getFeatures().hasFeature(CardFeatureType.STUNNED))
				&& filterHasNUUID.isGood(card.getFeatures().hasNUUID(handler))
			) {
				cards.add(card);
			}
		}
		return cards;
	}
	
}
