package fr.tangv.sorcicubeapp.component;

import javax.swing.JTextField;

public class ComponentText extends ComponentCustom {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JTextField text;
		
	public ComponentText(String name) {
		super(name);
		this.text = new JTextField("");
		this.add(text);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public String getText() {
		return this.text.getText();
	}
	
}
