package fr.tangv.sorcicubespell.logi;

import java.util.Collection;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardCible;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.card.CardRarity;
import fr.tangv.sorcicubespell.card.CardType;

public class PanelFilter extends JPanel {

	private static final long serialVersionUID = -6789820987468634151L;
	private PanelFilterEnum<CardType> filterType;
	private PanelFilterEnum<CardRarity> filterRarity;
	private PanelFilterEnum<CardFaction> filterFaction;
	private PanelFilterEnum<CardFaction> filterCibleFaction;
	private PanelFilterEnum<CardCible> filterCible;
	
	public PanelFilter() throws Exception {
		//init value
		this.filterType = new PanelFilterEnum<CardType>(CardType.ENTITY, "Type", BoxLayout.X_AXIS, false);
		this.filterRarity = new PanelFilterEnum<CardRarity>(CardRarity.COMMUN, "Rarity", BoxLayout.Y_AXIS, false);
		this.filterFaction = new PanelFilterEnum<CardFaction>(CardFaction.BASIC, "Faction", BoxLayout.Y_AXIS, false);
		this.filterCibleFaction = new PanelFilterEnum<CardFaction>(CardFaction.BASIC, "Cible Faction", BoxLayout.Y_AXIS, false);
		this.filterCible = new PanelFilterEnum<CardCible>(CardCible.ALL, "Cible", 0, true);
		//init gui
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(filterType);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(filterRarity);
		panel1.add(filterFaction);
		panel1.add(filterCibleFaction);
		this.add(panel1);
		this.add(filterCible);
	}
	
	public boolean hasFilter() {
		
		return false;
	}
	
	public Vector<Card> applyFilter(Collection<Card> list) {
		Vector<Card> cards = new Vector<Card>();
		for (Card card : list) {
			cards.add(card);//apply filter
		}
		return cards;
	}
	
}
