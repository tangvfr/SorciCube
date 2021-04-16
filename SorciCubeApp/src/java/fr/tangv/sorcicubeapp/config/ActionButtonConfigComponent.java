package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import fr.tangv.sorcicubeapp.utils.ClickListener;

public class ActionButtonConfigComponent extends JComponent {

	private static final long serialVersionUID = -7464669432995600532L;

	public ActionButtonConfigComponent(String name, Runnable run) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JButton btn = new JButton(name);
		btn.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (run != null)
					run.run();
			}
		});
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		btn.setHorizontalAlignment(JButton.LEFT);
		this.add(btn);
	}
	
}
