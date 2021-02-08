package fr.tangv.sorcicubeapp.tabbed;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

public class ComponentNumberInt extends JComponent {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JSpinner number;
		
	public ComponentNumberInt(String name) {
		//panel
		this.setBorder(new TitledBorder(name));
		this.setLayout(new GridLayout(1, 2, 5, 5));
		this.number = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		JLabel label = new JLabel(name+":");
		label.setHorizontalAlignment(JLabel.RIGHT);	
		this.add(label);
	}
	
	public void setInt(int number) {
		this.number.setValue(number);
	}
	
	public int getInt() {
		return (int) this.number.getValue();
	}
	
}