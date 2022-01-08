package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.tangv.sorcicubeapp.utils.ClickListener;

public class ConfigComponent extends JComponent {

	private static final long serialVersionUID = -3020980971136228749L;
	private final JLabel label;
	
	public ConfigComponent(String name) {
		//panel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		label = new JLabel(name+": ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		this.add(label);
	}
	
	public void addRemoveBtn(Runnable[] run) {
		if (run.length == 2) {
			JPanel pan = new JPanel(new GridLayout(1, 2));
			//btn1
			JButton btn1 = new JButton("\uD83D\uDCC4");
			btn1.addMouseListener(new ClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					run[0].run();
				}
			});
			btn1.setMaximumSize(new Dimension(getMinimumSize().width, ButtonConfigComponent.HEIGHT));
			btn1.setHorizontalAlignment(JButton.CENTER);
			pan.add(btn1);
			//btn2
			JButton btn2 = new JButton("\u274C");
			btn2.addMouseListener(new ClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					run[1].run();
				}
			});
			btn2.setMaximumSize(new Dimension(getMinimumSize().width, ButtonConfigComponent.HEIGHT));
			btn2.setHorizontalAlignment(JButton.CENTER);
			pan.add(btn2);
			//pan
			this.add(pan);
		}
	}
	
}
