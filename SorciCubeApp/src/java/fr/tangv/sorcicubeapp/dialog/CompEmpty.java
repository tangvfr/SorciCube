package fr.tangv.sorcicubeapp.dialog;

import java.awt.Dimension;

import javax.swing.JComponent;

public class CompEmpty extends JComponent {

	private static final long serialVersionUID = 7124141040180929487L;

	public CompEmpty(int width, int height) {
		this(new Dimension(width, height));
	}
	
	public CompEmpty(Dimension dim) {
		this.setSize(dim);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
	}
	
}
