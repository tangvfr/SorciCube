package fr.tangv.sorcicubespell.cards;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import fr.tangv.sorcicubespell.logi.FeaturesTableModel;

public class FeaturesTable extends JTable {

	private static final long serialVersionUID = 3573798367579198241L;
	
	public FeaturesTable(CardFeatures cardFeatures, boolean locked) {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setModel(new FeaturesTableModel(cardFeatures, locked));
		//add popupmenu
		//add click event
	}
	
}
