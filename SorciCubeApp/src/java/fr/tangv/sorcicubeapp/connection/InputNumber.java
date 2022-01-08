package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class InputNumber extends JPanel {

	private static final long serialVersionUID = -38172638634736085L;

	private final JLabel label;
	private final JSpinner spinner;
	
	public InputNumber(String name, int value, int max) {
		this.setLayout(new BorderLayout());
		this.label = new JLabel(name+": ");
		this.spinner = new JSpinner(new SpinnerNumberModel(value, 0, max, 1));
		this.add(label, BorderLayout.NORTH);
		this.add(spinner, BorderLayout.SOUTH);
	}
	
	public int getInput() {
		return (int) spinner.getValue();
	}
	
}