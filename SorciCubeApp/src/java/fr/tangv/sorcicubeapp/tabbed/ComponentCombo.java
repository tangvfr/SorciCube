package fr.tangv.sorcicubeapp.tabbed;

import java.util.Vector;

import javax.swing.JComboBox;

public class ComponentCombo<E> extends ComponentCustom {

	private static final long serialVersionUID = -5000586127613078036L;
	private final JComboBox<E> combo;
	
	public ComponentCombo(String name, Vector<E> list) {
		super(name);
		this.combo = new JComboBox<E>(list);
		this.add(combo);
	}
	
	@SuppressWarnings("unchecked")
	public E getSelection() {
		return (E) combo.getSelectedItem();
	}
	
	public void setSelection(E value) {
		combo.setSelectedItem(value);
	}
	
}
