package fr.tangv.sorcicubeapp.connection;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputText extends JPanel {

	private static final long serialVersionUID = -38172638634736084L;

	private final JLabel label;
	private final JTextField field;
	
	public InputText(String name, String value) {
		this.setLayout(new BorderLayout());
		this.label = new JLabel(name+": ");
		this.field = new JTextField(value);
		this.add(label, BorderLayout.NORTH);
		this.add(field, BorderLayout.SOUTH);
	}
	
	public String getInput() {
		return field.getText();
	}
	
}
