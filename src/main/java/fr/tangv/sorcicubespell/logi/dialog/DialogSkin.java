package fr.tangv.sorcicubespell.logi.dialog;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public abstract class DialogSkin extends DialogBase<JTextField> {
	
	private static final long serialVersionUID = 2834671283964685106L;
	
	public DialogSkin(Window owner, String label, String text) {
		super(owner, label, new JTextField(text.replaceFirst("http://textures.minecraft.net/texture/", "")));
		if (text.startsWith("skull: ") || text.startsWith("http://textures.minecraft.net/texture/")) {
			String link = text.startsWith("skull: ") ? text.replace("skull: ", "http://textures.minecraft.net/texture/") : text;
			try {
				BufferedImage imgB = ImageIO.read(new URL(link).openStream());
				Image img = imgB.getScaledInstance(imgB.getWidth()*4, imgB.getHeight()*4, Image.SCALE_DEFAULT);
				this.panUp.add(new JLabel(new ImageIcon(img)), BorderLayout.SOUTH);
			} catch (Exception e) {}
		}
		this.pack();
	}

}
