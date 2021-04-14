package fr.tangv.sorcicubeapp.config;

import javax.swing.JFrame;

import fr.tangv.sorcicubecore.config.ConfigParseException;

public class Testframe {

	public static void main(String[] args) throws ConfigParseException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		MainConfigPanel main = new MainConfigPanel();
		main.setView(new AbstractConfigPanel(main, null, "TEST PTDR", new TestConfig(null)));
		frame.setContentPane(main);
		frame.setVisible(true);
	}
	
}
