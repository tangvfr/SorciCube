package fr.tangv.sorcicubespell.logi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardFeatureType;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;

public class PanelFilter extends JPanel {

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
	
	public PanelFilter() throws Exception {
		//init value
		this.filterType = new PanelFilterEnum<CardType>(CardType.ENTITY, "Type", BoxLayout.X_AXIS, false);
		this.filterRarity = new PanelFilterEnum<CardRarity>(CardRarity.COMMUN, "Rarity", BoxLayout.Y_AXIS, false);
		this.filterFaction = new PanelFilterEnum<CardFaction>(CardFaction.BASIC, "Faction", BoxLayout.Y_AXIS, false);
		this.filterCibleFaction = new PanelFilterEnum<CardFaction>(CardFaction.BASIC, "Cible Faction", BoxLayout.Y_AXIS, false);
		this.filterCible = new PanelFilterEnum<CardCible>(CardCible.ALL, "Cible", 0, true);
		this.filterOriginalName = new PanelFilterBoolean("Orignal Name", "True", "False", "Any");
		this.filterHideCard = new PanelFilterBoolean("Hide Card", "True", "False", "Any");
		this.filterHasSkin = new PanelFilterBoolean("Has Skin", "True", "False", "Any");
		this.filterHasIncitement = new PanelFilterBoolean("Has Incitement", "True", "False", "Any");
		this.filterIsExited = new PanelFilterBoolean("Is Exited", "True", "False", "Any");
		this.filterIsInvulnerability = new PanelFilterBoolean("Is Invulnerability", "True", "False", "Any");
		this.filterIsImmobilization = new PanelFilterBoolean("Is Immobilization", "True", "False", "Any");
		this.filterIsStunned = new PanelFilterBoolean("Is Stunned", "True", "False", "Any");
		//init gui
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(filterType);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(filterRarity);
		panel1.add(filterFaction);
		panel1.add(filterCibleFaction);
		this.add(panel1);
		this.add(filterOriginalName);
		this.add(filterHideCard);
		this.add(filterHasSkin);
		this.add(filterHasIncitement);
		this.add(filterIsExited);
		this.add(filterIsInvulnerability);
		this.add(filterIsImmobilization);
		this.add(filterIsStunned);
		this.add(filterCible);
	}
	
	public Vector<Card> applyFilter(Collection<Card> list) {
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
				&& filterHideCard.isGood(card.getFeatures().hasFeature(CardFeatureType.HIDE_CART))
				&& filterHasSkin.isGood(card.getFeatures().hasFeature(CardFeatureType.SKIN))
				&& filterHasIncitement.isGood(card.getFeatures().hasFeature(CardFeatureType.INCITEMENT))
				&& filterIsExited.isGood(card.getFeatures().hasFeature(CardFeatureType.EXCITED))
				&& filterIsInvulnerability.isGood(card.getFeatures().hasFeature(CardFeatureType.INVULNERABILITY))
				&& filterIsImmobilization.isGood(card.getFeatures().hasFeature(CardFeatureType.IMMOBILIZATION))
				&& filterIsStunned.isGood(card.getFeatures().hasFeature(CardFeatureType.STUNNED)))
			{
				cards.add(card);
			}
		}
		return cards;
	}
	
}
