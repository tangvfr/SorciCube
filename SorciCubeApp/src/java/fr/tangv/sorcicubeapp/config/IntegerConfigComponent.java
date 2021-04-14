package fr.tangv.sorcicubeapp.config;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tangv.sorcicubecore.config.IntegerConfig;

public class IntegerConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = 5825619911390026882L;
	private final JSpinner number;
	
	public IntegerConfigComponent(IntegerConfig integer, String name) {
		super(name);
		this.number = new JSpinner(new SpinnerNumberModel(integer.value, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		this.number.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("int change");
				integer.value = (int) number.getValue();
			}
		});
		this.add(number);
	}
	
}
