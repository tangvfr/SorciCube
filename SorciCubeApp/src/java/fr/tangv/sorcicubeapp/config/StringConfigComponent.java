package fr.tangv.sorcicubeapp.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextArea;

import fr.tangv.sorcicubeapp.component.ComponentCustom;
import fr.tangv.sorcicubecore.config.StringConfig;

public class StringConfigComponent  extends ComponentCustom {

	private static final long serialVersionUID = 5825619911390026882L;
	private final JTextArea text;
	
	public StringConfigComponent(StringConfig string, String name) {
		super(name);
		this.text = new JTextArea(string.value);
		this.text.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("string change");
				string.value = text.getText();
			}
		});
		this.add(text);
	}
	
}