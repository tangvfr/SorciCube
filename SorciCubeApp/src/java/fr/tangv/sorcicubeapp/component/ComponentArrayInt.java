package fr.tangv.sorcicubeapp.component;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ComponentArrayInt extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 5937702865253871120L;
	private final JSpinner[] ints;
	private final JLabel total;
	
	private static String[] getListNames(Enum<?>[] enumm) {
		String[] listNames = new String[enumm.length];
		for (int i = 0; i < enumm.length; i++)
			listNames[i] = enumm[i].name();
		return listNames;
	}
	
	public ComponentArrayInt(Enum<?> enumm[], String name) {
		this(getListNames(enumm), name);
	}
		
	public ComponentArrayInt(String[] list, String name) {
		this.ints = new JSpinner[list.length];
		//panel
		this.setBorder(new TitledBorder(name));
		this.setLayout(new GridLayout(list.length+1, 2, 5, 5));
		for (int i = 0; i < list.length; i++) {
			JLabel label = new JLabel(list[i]+":");
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
			spinner.addChangeListener(this);
			this.add(spinner);
			ints[i] = spinner;
		}
		JLabel label = new JLabel("Total:");
		label.setHorizontalAlignment(JLabel.RIGHT);	
		this.add(label);
		this.total = new JLabel("0");
		this.total.setForeground(new Color(139, 0, 0));
		this.add(this.total);
	}
	
	public void setInts(int[] values) {
		for (int i = 0; i < ints.length; i++)
			ints[i].setValue(values[i]);
	}
	
	public int[] getInts() {
		int[] values = new int[ints.length];
		for (int i = 0; i < values.length; i++)
			values[i] = (int) ints[i].getValue();
		return values;
	}

	public int calcTotal() {
		int total = 0;
		for (int i = 0; i < ints.length; i++)
			total += (int) ints[i].getValue();
		return total;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		this.total.setText(Integer.toString(calcTotal()));
	}
	
}
