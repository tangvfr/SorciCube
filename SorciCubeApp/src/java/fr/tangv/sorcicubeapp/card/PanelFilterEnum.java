package fr.tangv.sorcicubeapp.card;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.dialog.CompEmpty;
import fr.tangv.sorcicubecore.card.CardCible;

public class PanelFilterEnum<T extends Enum<?>> extends JPanel {

	private static final long serialVersionUID = 5956915292783604068L;
	private ConcurrentHashMap<T, JCheckBox> map;
	
	public PanelFilterEnum(T enumm, String name, int axis, boolean cible) throws Exception {
		this.map = new ConcurrentHashMap<T, JCheckBox>();
		if (cible)
			this.setLayout(new GridLayout(10, 2));
		else
			this.setLayout(new BoxLayout(this, axis));
		this.setBorder(new TitledBorder(name));
		Method values = enumm.getClass().getDeclaredMethod("values", new Class<?>[0]);
		@SuppressWarnings("unchecked")
		T[] list = (T[]) values.invoke(enumm, new Object[0]);
		for (T value : list) {
			String label;
			if (value == CardCible.ONE_ENTITY_ALLY_AND_ONE_ENTITY_ENEMIE) {
				label = "ONE_EA_AND_ONE_EE";
			} else
				label = value.toString();
			JCheckBox checkBox = new JCheckBox(label, true);
			this.map.put(value, checkBox);
			this.add(checkBox);
		}
		this.add(new CompEmpty(10, 10));
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
