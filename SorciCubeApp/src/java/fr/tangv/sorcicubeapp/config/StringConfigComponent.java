package fr.tangv.sorcicubeapp.config;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.tangv.sorcicubecore.config.StringConfig;

public class StringConfigComponent  extends ConfigComponent {

	private static final long serialVersionUID = 5825619911390026882L;
	private final JTextArea text;
	
	public StringConfigComponent(StringConfig string, String name) {
		super(name);
		this.text = new JTextArea(string.value);
		this.text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				string.value = text.getText();
			}
		});
		this.add(text);
	}
	
}