package fr.tangv.sorcicubeapp.config;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tangv.sorcicubecore.config.VectorConfig;

public class VectorConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = -7884360632625565772L;
	
	public VectorConfigComponent(VectorConfig vector, String name, Runnable run) {
		super(name);
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(3, 2, 5, 5));
		pan.setBorder(new TitledBorder("Vector"));
		//x
		JSpinner x = new JSpinner(new SpinnerNumberModel(vector.x, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		x.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				vector.x = (double) x.getValue();
			}
		});
		//y
		JSpinner y = new JSpinner(new SpinnerNumberModel(vector.y, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		y.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				vector.y = (double) y.getValue();
			}
		});
		//z
		JSpinner z = new JSpinner(new SpinnerNumberModel(vector.z, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		z.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				vector.z = (double) z.getValue();
			}
		});
		//add
		pan.add(new Lab("x"));
		pan.add(x);
		pan.add(new Lab("y"));
		pan.add(y);
		pan.add(new Lab("z"));
		pan.add(z);
		this.add(pan);
		addRemoveBtn(run);
	}
	
	private static final class Lab extends JLabel {
		
		private static final long serialVersionUID = -7666430764555374366L;

		public Lab(String name) {
			super(name+":");
			setHorizontalAlignment(Lab.RIGHT);
		}
		
	}
	
}
