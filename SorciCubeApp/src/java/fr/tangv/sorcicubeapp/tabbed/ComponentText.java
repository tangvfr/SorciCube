package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ComponentText extends JComponent {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JTextField text;
		
	public ComponentText(String name) {
		//panel
		this.setBorder(new TitledBorder(name));
		this.setLayout(new GridLayout(1, 2, 5, 5));
		this.text = new JTextField("");
		JLabel label = new JLabel(name+":");
		label.setHorizontalAlignment(JLabel.RIGHT);	
		this.add(label);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public String getText() {
		return this.text.getText();
	}
	
}
