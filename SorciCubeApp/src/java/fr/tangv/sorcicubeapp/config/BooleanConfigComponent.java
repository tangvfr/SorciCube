package fr.tangv.sorcicubeapp.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import fr.tangv.sorcicubecore.config.BooleanConfig;

public class BooleanConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = -6765183340444651119L;
	private final JComboBox<String> combo;
	
	public BooleanConfigComponent(BooleanConfig bool, String name) {
		super(name);
		this.combo = new JComboBox<String>(new String[] {"true", "false"});
		this.combo.setSelectedItem(bool.value ? "true" : "false");
		this.combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getID() == ActionEvent.ACTION_FIRST) {
					System.out.println("bool actual");
					bool.value = combo.getSelectedItem().equals("true");
				}
			}
		});
		this.add(combo);
	}
	
}
