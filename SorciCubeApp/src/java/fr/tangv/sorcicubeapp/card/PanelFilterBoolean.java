package fr.tangv.sorcicubeapp.card;

import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class PanelFilterBoolean extends JPanel {

	private static final long serialVersionUID = 7488857843123501616L;
	private JRadioButton radioTrue;
	private JRadioButton radioFalse;
	private JRadioButton radioAny;
	
	public PanelFilterBoolean(String title, String textTrue, String textFalse, String textAny) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(new TitledBorder(title));
		//init
		this.radioTrue = new JRadioButton(textTrue, false);
		radioTrue.addActionListener((ActionEvent e) -> {
			if (e.getID() == 1001) {
				radioTrue.setSelected(true);
				radioFalse.setSelected(false);
				radioAny.setSelected(false);
			}
		});
		this.radioFalse = new JRadioButton(textFalse, false);
		radioFalse.addActionListener((ActionEvent e) -> {
			if (e.getID() == 1001) {
				radioTrue.setSelected(false);
				radioFalse.setSelected(true);
				radioAny.setSelected(false);
			}
		});
		this.radioAny = new JRadioButton(textAny, true);
		radioAny.addActionListener((ActionEvent e) -> {
			if (e.getID() == 1001) {
				radioTrue.setSelected(false);
				radioFalse.setSelected(false);
				radioAny.setSelected(true);
			}
		});
		//display
		this.add(radioTrue);
		this.add(radioFalse);
		this.add(radioAny);
	}
	
	public boolean isGood(boolean value) {
		if (radioAny.isSelected())
			return true;
		else
			return radioTrue.isSelected() == value;
	}
	
}
