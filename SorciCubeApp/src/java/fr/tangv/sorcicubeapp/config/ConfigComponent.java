package fr.tangv.sorcicubeapp.config;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class ConfigComponent extends JComponent {

	private static final long serialVersionUID = -3020980971136228749L;

	public ConfigComponent(String name) {
		//panel
		this.setLayout(new GridLayout(1, 2, 5, 5));
		JLabel label = new JLabel(name+":");
		label.setHorizontalAlignment(JLabel.RIGHT);
		this.add(label);
	}
	
}
