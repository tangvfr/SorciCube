package fr.tangv.sorcicubeapp.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;

import fr.tangv.sorcicubeapp.component.ComponentCustom;
import fr.tangv.sorcicubecore.config.BooleanConfig;

public class BooleanConfigComponent extends ComponentCustom {

	private static final long serialVersionUID = -6765183340444651119L;
	private final JComboBox<String> combo;
	
	public BooleanConfigComponent(BooleanConfig bool, String name) {
		super(name);
		this.combo = new JComboBox<String>(new String[] {"true", "false"});
		this.combo.setSelectedItem(bool.value ? "true" : "false");
		this.combo.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("bool actual");
				bool.value = combo.getSelectedItem().equals("true");
			}
		});
		this.add(combo);
	}
	
}
