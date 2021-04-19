package fr.tangv.sorcicubeapp.component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ComponentNumberInt extends ComponentCustom {

	private static final long serialVersionUID = 5937702865253871122L;
	private final JSpinner number;
	private final int min;
	private final int max;
		
	public ComponentNumberInt(String name, int min, int max, int step) {
		super(name);
		this.min = min;
		this.max = max;
		this.number = new JSpinner(new SpinnerNumberModel(0, min, max, step));
		this.add(number);
	}
	
	public void setInt(int number) {
		if (number < min)
			number = min;
		else if (number > max)
			number = max;
		this.number.setValue(number);
	}
	
	public int getInt() {
		return (int) this.number.getValue();
	}
	
}
