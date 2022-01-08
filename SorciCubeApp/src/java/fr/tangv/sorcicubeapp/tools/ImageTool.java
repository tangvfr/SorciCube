package fr.tangv.sorcicubeapp.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import fr.tangv.sorcicubeapp.utils.ClickListener;

public class ImageTool extends JPanel {

	private static final long serialVersionUID = 1092030512998823501L;
	private JButton fileBtn;
	private JButton renderBtn;
	private JTextField urlText;
	
	public ImageTool() {
		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(new TitledBorder("Image Format Tool"));
		Dimension dim = new Dimension(500, 110);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		urlText = new JTextField("file or url");
		fileBtn = new JButton("File");
		fileBtn.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				File file = new File("./");
				try {
					String text = urlText.getText();
					if (!text.isEmpty() && !text.startsWith("http") && !text.equals("file or url"))
						file = new File(urlText.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				fc.setSelectedFile(file);
				fc.setCurrentDirectory(file);
				if (fc.showOpenDialog(ImageTool.this) == JFileChooser.APPROVE_OPTION) {
					urlText.setText(fc.getSelectedFile().getPath());
				}
			}
		});
		fileBtn.setFocusable(false);
		renderBtn = new JButton("Render");
		renderBtn.addMouseListener(new ClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String text = urlText.getText();
				if (text.isEmpty())
					JOptionPane.showMessageDialog(ImageTool.this, "Path is empty !", "Tool Error", JOptionPane.ERROR_MESSAGE);
				else
					try {
						BufferedImage img = text.startsWith("http") ? ImageIO.read(new URL(text).openStream()) : ImageIO.read(new File(text));
						if (img.getWidth() == 64 && (img.getHeight() == 32 || img.getHeight() == 64)) {
							BufferedImage render = new BufferedImage(64, 32, BufferedImage.TYPE_4BYTE_ABGR);
							Graphics g = render.getGraphics();
							//head
							g.drawImage(img.getSubimage(0, 0, 32, 16), 0, 0, null);
							g.drawImage(img.getSubimage(32, 0, 32, 16), 0, 0, null);
							//body
							g.drawImage(img.getSubimage(0, 16, 64, 16), 0, 16, null);
							if (img.getHeight() == 64)
								g.drawImage(img.getSubimage(0, 32, 64, 16), 0, 16, null);
							g.dispose();
							BufferedImage demo = new BufferedImage(160, 64, BufferedImage.TYPE_4BYTE_ABGR);
							Graphics dg = demo.getGraphics();
							dg.drawImage(img, 0, 0, null);
							dg.drawImage(render, 96, 0, null);
							dg.setColor(Color.LIGHT_GRAY);
							dg.fillPolygon(new int[] {72, 88, 72}, new int[] {29, 32, 36}, 3);
							dg.dispose();
							if (JOptionPane.showConfirmDialog(ImageTool.this, new JLabel(new ImageIcon(demo.getScaledInstance(160*5, 64*5, BufferedImage.SCALE_DEFAULT))), "Tool Render" , JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
								return;
							JFileChooser fc = new JFileChooser();
							fc.setMultiSelectionEnabled(false);
							File cd = new File("./render.png");
							try {
								if (!urlText.getText().startsWith("http"))
									cd = new File(urlText.getText().replace(".png", "")+"-render.png");
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							fc.setSelectedFile(cd);
							if (fc.showSaveDialog(ImageTool.this) == JFileChooser.APPROVE_OPTION) {
								String path = fc.getSelectedFile().getPath();
								if (!path.endsWith(".png"))
									path += ".png";
								File file = new File(path);
								if (file.exists()) {
									if (!file.isFile()) {
										JOptionPane.showMessageDialog(ImageTool.this, "File is folder!", "Tool Error", JOptionPane.ERROR_MESSAGE);
										return;
									} else {
										if (JOptionPane.showConfirmDialog(ImageTool.this, "Do you sure replace that file !", "Tool Replace" , JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
											return;
									}
								} else {
									file.createNewFile();
								}
								ImageIO.write(render, "PNG", file);
							}
						} else {
							JOptionPane.showMessageDialog(ImageTool.this, "Image size invalid !", "Tool Error", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(ImageTool.this, e1.getMessage(), "Tool Error", JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		renderBtn.setFocusable(false);
		this.add(fileBtn, BorderLayout.NORTH);
		this.add(urlText, BorderLayout.CENTER);
		this.add(renderBtn, BorderLayout.SOUTH);
	}
	
}
