package fr.tangv.sorcicubespell.logi.tools;

import javax.swing.JFrame;

public class TestTools {

	public static void main(String[] args) {
		JFrame frame = new JFrame("TestTools");
		frame.setContentPane(new PanelTools());
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
