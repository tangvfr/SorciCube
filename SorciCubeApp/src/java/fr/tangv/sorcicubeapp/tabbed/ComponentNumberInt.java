package fr.tangv.sorcicubeapp.tabbed;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ComponentNumberInt extends ComponentCustom {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JSpinner number;
		
	public ComponentNumberInt(String name) {
		super(name);
		this.number = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		this.add(number);
	}
	
	public void setInt(int number) {
		this.number.setValue(number);
	}
	
	public int getInt() {
		return (int) this.number.getValue();
	}
	
}
