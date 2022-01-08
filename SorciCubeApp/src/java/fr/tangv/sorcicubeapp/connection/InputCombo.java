package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputCombo<T extends Enum<?>> extends JPanel {

	private static final long serialVersionUID = -38172638634736185L;

	private final JLabel label;
	private JComboBox<T> combo;
	
	public InputCombo(String name, T enumm) {
		this.setLayout(new BorderLayout());
		this.label = new JLabel(name+": ");
		try {
			Method method = enumm.getClass().getMethod("values", new Class<?>[0]);
			@SuppressWarnings("unchecked")
			T[] list = (T[]) method.invoke(enumm, new Object[0]);
			this.combo = new JComboBox<T>(list);
			this.combo.setSelectedItem(enumm);
		} catch (Exception e) {
			e.printStackTrace();
			this.combo = new JComboBox<T>();
		}
		this.add(label, BorderLayout.NORTH);
		this.add(combo, BorderLayout.SOUTH);
	}
	
	@SuppressWarnings("unchecked")
	public T getCombo() {
		return (T) combo.getSelectedItem();
	}
	
}
