package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

public abstract class ComponentCustom extends JComponent {
		
	private static final long serialVersionUID = -3474181815507946061L;

	public ComponentCustom(String name) {
		//panel
		this.setBorder(new TitledBorder(name));
		this.setLayout(new GridLayout(1, 2, 5, 5));
		JLabel label = new JLabel(name+":");
		label.setHorizontalAlignment(JLabel.RIGHT);	
		this.add(label);
	}
	
}
