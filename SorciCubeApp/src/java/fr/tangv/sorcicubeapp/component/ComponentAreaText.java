package fr.tangv.sorcicubeapp.component;

import javax.swing.JTextArea;

public class ComponentAreaText extends ComponentCustom {

	private static final long serialVersionUID = 8565604831278198626L;
	private final JTextArea area;
	
	public ComponentAreaText(String name) {
		super(name);
		this.area = new JTextArea("");
		area.setLineWrap(false);
		this.add(area);
	}
	
	public void setArea(String area) {
		this.area.setText(area);
	}
	
	public String getArea() {
		return this.area.getText();
	}
	
}
