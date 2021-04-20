package fr.tangv.sorcicubeapp.config;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tangv.sorcicubecore.config.IntegerConfig;

public class IntegerConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = 5825619911390026882L;
	
	public IntegerConfigComponent(IntegerConfig integer, String name, Runnable[] run) {
		super(name);
		JSpinner number = new JSpinner(new SpinnerNumberModel(integer.value, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		number.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				integer.value = (int) number.getValue();
			}
		});
		this.add(number);
		addRemoveBtn(run);
	}
	
}
