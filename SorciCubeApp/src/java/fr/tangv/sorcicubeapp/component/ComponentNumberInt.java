package fr.tangv.sorcicubeapp.component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ComponentNumberInt extends ComponentCustom {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JSpinner number;
		
	public ComponentNumberInt(String name, int min, int max, int step) {
		super(name);
		this.number = new JSpinner(new SpinnerNumberModel(0, min, max, step));
		this.add(number);
	}
	
	public void setInt(int number) {
		this.number.setValue(number);
	}
	
	public int getInt() {
		return (int) this.number.getValue();
	}
	
}
