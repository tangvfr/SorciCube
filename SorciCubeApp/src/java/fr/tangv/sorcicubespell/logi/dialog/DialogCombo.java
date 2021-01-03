package fr.tangv.sorcicubespell.logi.dialog;

import java.awt.Window;
import java.lang.reflect.Field;
import java.util.Vector;

import javax.swing.JComboBox;

public abstract class DialogCombo<T extends Enum<?>> extends DialogBase<JComboBox<String>> {

	private static final long serialVersionUID = 1742967987920122067L;
	private T enumCombo;
	
	private static JComboBox<String> generatedCombo(Enum<?> enumCombo) {
		Vector<String> comboList = new Vector<String>();
		for (Field field : enumCombo.getClass().getFields())
			comboList.add(field.getName());
		JComboBox<String> combo = new JComboBox<String>(comboList);
		combo.setSelectedItem(enumCombo.name());
		return combo;
	}
	
	public DialogCombo(Window owner, String label, T enumCombo) {
		super(owner, label, generatedCombo(enumCombo));
		this.enumCombo = enumCombo;
	}

	@Override
	public void eventOk(JComboBox<String> comp) {
		this.eventOk(this.valueOf((String) comp.getSelectedItem()));
	}
	
	@SuppressWarnings("unchecked")
	private T valueOf(String name) {
		try {
			for (Field field : enumCombo.getClass().getFields())
				if (field.getName().equals(name))
						return (T) field.get(enumCombo);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public abstract void eventOk(T enumCombo);
	
}
