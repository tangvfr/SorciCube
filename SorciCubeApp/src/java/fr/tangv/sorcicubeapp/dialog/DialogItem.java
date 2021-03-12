package fr.tangv.sorcicubeapp.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tangv.sorcicubecore.card.CardMaterial;

public abstract class DialogItem extends DialogBase<JTextField> {

	private static final long serialVersionUID = 2834671277764685106L;
	public static Items items;
	private static final int SIZE = 256;
	
	static {
		try {
			items = new Items();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DialogItem(Window owner, String action, String label, CardMaterial material) {
		super(owner, action, label, new JTextField(material.toString()));
		Item item = items.get(material.toString());
		Image img;
		String name;
		if (item != null) {
			img = item.img.getScaledInstance(SIZE, SIZE, BufferedImage.SCALE_DEFAULT);
			name = item.name;
		} else {
			img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = ((BufferedImage) img).getGraphics();
			g.setColor(Color.RED);
			g.fillRect(0, 0, SIZE, SIZE);
			g.dispose();
			name = "unknown";
		}
		JPanel pan = new JPanel(new BorderLayout(5, 5));
		pan.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
		pan.add(new JLabel(name), BorderLayout.SOUTH);
		//add panUP
		this.panUp.add(pan, BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(owner);
	}
	
}
