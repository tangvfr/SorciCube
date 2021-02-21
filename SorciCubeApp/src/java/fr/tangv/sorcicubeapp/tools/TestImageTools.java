package fr.tangv.sorcicubeapp.tools;

import javax.swing.JFrame;

public class TestImageTools {

	public static void main(String[] args) {
		JFrame frame = new JFrame("TestTools");
		frame.setContentPane(new ImageTool());
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
