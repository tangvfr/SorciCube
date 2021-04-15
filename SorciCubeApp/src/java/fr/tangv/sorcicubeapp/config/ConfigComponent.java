package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

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
	
	public void addRemoveBtn(Runnable run) {
		if (run != null) {
			JButton btn = new JButton("\u274C");
			btn.addMouseListener(new ClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					run.run();
				}
			});
			btn.setMaximumSize(new Dimension(getMinimumSize().width, ButtonConfigComponent.HEIGHT));
			btn.setHorizontalAlignment(JButton.CENTER);
			this.add(btn);
		}
	}
	
}
