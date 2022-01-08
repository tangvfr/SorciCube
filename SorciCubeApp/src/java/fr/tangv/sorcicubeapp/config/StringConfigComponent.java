package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.tangv.sorcicubecore.config.StringConfig;

public class StringConfigComponent  extends ConfigComponent {

	private static final long serialVersionUID = 5825619911390026882L;
	
	public StringConfigComponent(StringConfig string, String name, Runnable[] run) {
		super(name);
		JTextArea text = new JTextArea(string.value);
		text.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				string.value = text.getText();
				setMaximumSize(new Dimension(Integer.MAX_VALUE, getMinimumSize().height));
			}
		});
		this.add(text);
		addRemoveBtn(run);
	}
	
}