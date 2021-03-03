package fr.tangv.sorcicubeapp.dialog;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.tangv.sorcicubecore.card.CardMaterial;

public abstract class DialogItem extends DialogBase<JTextField> {

	private static final long serialVersionUID = 2834671277764685106L;
	
	public DialogItem(Window owner, String action, String label, CardMaterial material) {
		super(owner, action, label, new JTextField(material.toString()));
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR);
		this.panUp.add(new JLabel(new ImageIcon(img)), BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(owner);
	}
	
}
