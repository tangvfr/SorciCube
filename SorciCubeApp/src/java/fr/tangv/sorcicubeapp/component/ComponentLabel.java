package fr.tangv.sorcicubeapp.component;

import javax.swing.JTextField;

public class ComponentLabel extends ComponentCustom {

	private static final long serialVersionUID = -8565604831278198626L;
	private final JTextField label;
	
	public ComponentLabel(String name) {
		super(name);
		this.label = new JTextField("");
		label.setEditable(false);
		this.add(label);
	}
	
	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public String getLabel() {
		return this.label.getText();
	}
	
}
