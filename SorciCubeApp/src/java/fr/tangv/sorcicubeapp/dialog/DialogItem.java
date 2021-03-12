package fr.tangv.sorcicubeapp.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fr.tangv.sorcicubeapp.SorciCubeApp;
import fr.tangv.sorcicubecore.card.CardMaterial;

public abstract class DialogItem extends DialogBase<JTextField> {

	private static final long serialVersionUID = 2834671277764685106L;
	private static final int SIZE = 128;
	private static final int BORDER = 10;
	
	public DialogItem(Window owner, String action, String label, CardMaterial material) {
		super(owner, action, label, new JTextField(material.toString()));
		Item item = SorciCubeApp.items.get(material.toString());
		Image img;
		String name;
		if (item != null) {
			img = item.img.getScaledInstance(item.img.getWidth()*2, item.img.getHeight()*2, BufferedImage.SCALE_SMOOTH);
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
		pan.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		pan.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
		JLabel lab = new JLabel(name);
		lab.setHorizontalAlignment(JLabel.CENTER);
		pan.add(lab, BorderLayout.SOUTH);
		//add panUP
		this.panUp.add(pan, BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(owner);
	}
	
}
