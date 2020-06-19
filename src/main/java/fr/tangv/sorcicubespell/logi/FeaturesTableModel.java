package fr.tangv.sorcicubespell.logi;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubespell.cards.CardFeature;

public class FeaturesTableModel extends AbstractTableModel implements MouseListener {

	private static final long serialVersionUID = 506002420552780083L;
	private FeaturesTable featuresTable;
	
	public FeaturesTableModel(FeaturesTable featuresTable) {
		this.featuresTable = featuresTable;
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public int getRowCount() {
		return 1+featuresTable.getCardFeatures().size();
	}
	
	private static final String[] names = {"Name", "Type", "Value", "Type Value"};
	@Override
	public Object getValueAt(int row, int column) {
		String text = null;
		boolean lock;
		if (row == 0) {
			text = names[column];
			lock = false;
		} else {
			CardFeature feature = featuresTable.getCardFeatures().getFeature(featuresTable.getName(row-1));
			switch (column) {
				case 0://name
					text = feature.getName();
					break;
					
				case 1://type
					text = feature.getType().name();
					break;
					
				case 2://value
					text = feature.getValue().toString();
					break;
					
				case 3://type value
					text = feature.getValue().getType().name();
					break;
					
				default://default not possible
					break;
			}
			lock = featuresTable.isEntity() && (feature.getName().equals("Health") || feature.getName().equals("AttackDamage"));
		}
		if (column == 1 || column == 3 || lock)
			return "<html><body><span color='#5555FF'>"+text+"</span></body></html>";
		return text;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
