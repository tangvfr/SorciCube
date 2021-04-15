package fr.tangv.sorcicubeapp.config;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class ConfigComponent extends JComponent {

	private static final long serialVersionUID = -3020980971136228749L;
	private final JLabel label;
	
	public ConfigComponent(String name) {
		//panel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		label = new JLabel(name+":");
		label.setHorizontalAlignment(JLabel.RIGHT);
		this.add(label);
	}
	
}
