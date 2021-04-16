package fr.tangv.sorcicubeapp.config;

import javax.swing.JFrame;

import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.configs.Config;

public class Testframe {

	public static void main(String[] args) throws ConfigParseException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		MainConfigPanel main = new MainConfigPanel() {

			private static final long serialVersionUID = -6132496489904646459L;

			@Override
			public void save() {}

			@Override
			public void cancel() {}
			
		};
		main.setView(new AbstractConfigPanel(main, null, "TEST PTDR", new Config(null)));
		frame.setContentPane(main);
		frame.pack();
		frame.setVisible(true);
	}
	
}
