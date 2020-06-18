package fr.tangv.sorcicubespell.logi;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import fr.tangv.sorcicubespell.cards.CardFeature;
import fr.tangv.sorcicubespell.cards.CardFeatures;

public class FeaturesTableModel extends AbstractTableModel implements MouseListener {

	private static final long serialVersionUID = 506002420552780083L;
	private CardFeatures cardFeatures;
	//private boolean locked;
	private Map<Integer, String> mapName;
	
	public FeaturesTableModel(CardFeatures cardFeatures, boolean locked) {
		this.cardFeatures = cardFeatures;
		//this.locked = locked;
		this.mapName = new HashMap<Integer, String>();
		int i = 0;
		for (String name : cardFeatures.list()) {
			mapName.put(i, name);
			i++;
		}
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public int getRowCount() {
		return 1+cardFeatures.size();
	}
	
	private static final String[] names = {"Name", "Type", "Value", "Type Value"};
	@Override
	public Object getValueAt(int row, int column) {
		String text = null;
		if (row == 0) {
			text = names[column];
		} else {
			CardFeature feature = cardFeatures.getFeature(mapName.get(row-1));
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
		}
		if (column == 1 || column == 3)
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
