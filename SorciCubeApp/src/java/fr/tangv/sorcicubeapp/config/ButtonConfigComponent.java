package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.ListConfig;

public class ButtonConfigComponent extends ConfigComponent {

	private static final long serialVersionUID = -4041127060885134824L;
	public static final int HEIGHT = 25;
	
	public ButtonConfigComponent(ConfigPanel config, ElementConfig element, String name, Runnable[] run) {
		super(name);
		String deco = ((element instanceof AbstractConfig) ? "\uD83D\uDCC1" : (element instanceof ListConfig<?>) ? "\uD83D\uDD6E" : "?");
		JButton btn = new JButton("Enter "+deco);
		btn.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				config.enter(element, name);
			}
		});
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
		btn.setHorizontalAlignment(JButton.LEFT);
		this.add(btn);
		addRemoveBtn(run);
	}
	
}
