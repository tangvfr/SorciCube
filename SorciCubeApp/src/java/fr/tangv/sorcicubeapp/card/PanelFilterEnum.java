package fr.tangv.sorcicubeapp.card;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubecore.card.CardCible;

public class PanelFilterEnum<T extends Enum<?>> extends JPanel {

	private static final long serialVersionUID = 5956915292783604068L;
	private ConcurrentHashMap<T, JCheckBox> map;
	
	public PanelFilterEnum(T[] enums, String name, int val) throws Exception {
		this.map = new ConcurrentHashMap<T, JCheckBox>();
		if (val < 0)
			this.setLayout(new GridLayout((int) (((enums.length+1)/-(double)val)+0.5D), -val));
		else
			this.setLayout(new BoxLayout(this, val));
		this.setBorder(new TitledBorder(name));
		for (T value : enums) {
			String label;
			if (value == CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE) {
				label = "ONE_EA_AND_ONE_EE";
			} else
				label = value.toString();
			JCheckBox checkBox = new JCheckBox(label, true);
			this.map.put(value, checkBox);
			this.add(checkBox);
		}
		JCheckBox all = new JCheckBox("*All*", true);
		all.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == 1001) {
					for (JCheckBox box : map.values())
						box.setSelected(all.isSelected());
				}
			}
		});
		this.add(all);
	 }
	
	public ArrayList<T> makeFilter() {
		ArrayList<T> list = new ArrayList<T>();
		for (T key : map.keySet())
			if (map.get(key).isSelected())
				list.add(key);
		return list;
	}
	
}
