package fr.tangv.sorcicubeapp.config;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tangv.sorcicubecore.config.LocationConfig;

public class LocationConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = 7884360632625565772L;
	
	public LocationConfigComponent(LocationConfig loc, String name, Runnable run) {
		super(name);
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(6, 2, 5, 5));
		pan.setBorder(new TitledBorder("Location"));
		//world
		JTextField world = new JTextField(loc.world);
		world.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				loc.world = world.getText();
			}
		});
		//x
		JSpinner x = new JSpinner(new SpinnerNumberModel(loc.x, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		x.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				loc.x = (double) x.getValue();
			}
		});
		//y
		JSpinner y = new JSpinner(new SpinnerNumberModel(loc.y, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		y.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				loc.y = (double) y.getValue();
			}
		});
		//z
		JSpinner z = new JSpinner(new SpinnerNumberModel(loc.z, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0));
		z.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				loc.z = (double) z.getValue();
			}
		});
		//pitch
		JSpinner pitch = new JSpinner(new SpinnerNumberModel(loc.pitch, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 1.0));
		pitch.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				loc.pitch = (float) pitch.getValue();
			}
		});
		//yaw
		JSpinner yaw = new JSpinner(new SpinnerNumberModel(loc.yaw, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 1.0));
		yaw.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				loc.yaw = (float) yaw.getValue();
			}
		});
		//add
		pan.add(new Lab("world"));
		pan.add(world);
		pan.add(new Lab("x"));
		pan.add(x);
		pan.add(new Lab("y"));
		pan.add(y);
		pan.add(new Lab("z"));
		pan.add(z);
		pan.add(new Lab("pitch"));
		pan.add(pitch);
		pan.add(new Lab("yaw"));
		pan.add(yaw);
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