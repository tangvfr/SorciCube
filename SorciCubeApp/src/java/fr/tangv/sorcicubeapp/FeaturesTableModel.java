package fr.tangv.sorcicubeapp;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubecore.card.CardFeature;
import fr.tangv.sorcicubecore.card.CardFeatureType;
import fr.tangv.sorcicubecore.card.CardValue.TypeValue;

public class FeaturesTableModel extends AbstractTableModel implements MouseListener {

	private static final long serialVersionUID = 506002420552780083L;
	private FeaturesTable featuresTable;
	
	public FeaturesTableModel(FeaturesTable featuresTable) {
		this.featuresTable = featuresTable;
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public int getRowCount() {
		return 1+featuresTable.getCardFeatures().size();
	}
	
	private static final String[] names = {"Type", "Type Value", "Value"};
	@Override
	public Object getValueAt(int row, int column) {
		String text = null;
		boolean lock;
		if (row == 0) {
			text = names[column];
			lock = false;
		} else {
			CardFeature feature = featuresTable.getCardFeature(row-1);
			switch (column) {
				case 0://type
					text = feature.getType().name();
					break;
					
				case 1://type value
					text = feature.getValue().getType().name();
					break;
					
				case 2://value
					if (feature.getType() == CardFeatureType.SKIN)
						text = feature.getValue().toString().replace("http://textures.minecraft.net/texture/", "");
					else
						text = feature.getValue().toString();
					break;
					
				default://default not possible
					break;
			}
			lock = (column == 2 && feature.getType().getTypeValue() == TypeValue.NONE) || (column != 2 && featuresTable.isEntity() && (feature.getType() == CardFeatureType.DAMAGE || feature.getType() == CardFeatureType.HEALTH));
		}
		if (column == 1 || lock)
			return "<html><body><span color='#5555FF'>"+text+"</span></body></html>";
		return text;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
