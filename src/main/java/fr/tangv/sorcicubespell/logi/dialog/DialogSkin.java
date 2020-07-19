package fr.tangv.sorcicubespell.logi.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubespell.card.CardSkin;
import fr.tangv.sorcicubespell.logi.ClickListener;

public abstract class DialogSkin extends DialogBase<JTextField> {
	
	private static final long serialVersionUID = 2834671283964685106L;
	
	public DialogSkin(Window owner, String label, CardSkin skin, boolean head) {
		super(owner, label, new JTextField(head ? "skull: "+skin.toString() : skin.toString()));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		try {
			JLabel labTexture = new JLabel(new ImageIcon(skin.toImageTexture()));
			labTexture.setBorder(new EmptyBorder(0, 0, 0, 30));
			panel.add(labTexture);
			if (!skin.isLastVersion())
				panel.add(new JLabel(new ImageIcon(head ? skin.toImageHead() : skin.toImageSkin())));
			else {
				JLabel pan = new JLabel("Error Invalid Skin");
				pan.setSize(200, 200);
				pan.setBackground(Color.RED);
				panel.add(pan);
			}
		} catch (Exception e) {}
		this.panUp.add(panel, BorderLayout.SOUTH);
		panDown.add(new CompEmpty(10, 10));
		JButton btn = new JButton("mineskin.org");
		btn.setFocusable(false);
		btn.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE))
					try {
						Desktop.getDesktop().browse(new URI("https://mineskin.org/"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
			}
		});
		panDown.add(btn);
		this.pack();
		this.setLocationRelativeTo(owner);
	}

}
