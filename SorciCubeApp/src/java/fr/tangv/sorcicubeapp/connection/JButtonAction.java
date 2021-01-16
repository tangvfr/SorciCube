package fr.tangv.sorcicubeapp.connection;

import java.awt.event.MouseEvent;

import javax.swing.JButton;

import fr.tangv.sorcicubeapp.utils.ClickListener;

public class JButtonAction extends JButton {

	private static final long serialVersionUID = -3059632798489266080L;
	
	public JButtonAction(String name, Runnable run) {
		super(name);
		this.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				run.run();
			}
		});
		this.setFocusable(false);
	}
	
}
