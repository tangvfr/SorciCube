package fr.tangv.sorcicubeapp.tabbed;

import javax.swing.JCheckBox;

public class ComponentBoolean extends ComponentCustom {

	private static final long serialVersionUID = 3711647986771721888L;
	private final JCheckBox check;
		
	public ComponentBoolean(String name) {
		super(name);
		this.check = new JCheckBox();
		this.add(check);
	}
	
	public void setBoolean(boolean value) {
		check.setSelected(value);
	}
	
	public boolean getBoolean() {
		return check.isSelected();
	}
	
}
