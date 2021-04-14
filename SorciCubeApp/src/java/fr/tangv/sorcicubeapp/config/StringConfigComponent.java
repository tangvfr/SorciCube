package fr.tangv.sorcicubeapp.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

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
				System.out.println("string change");
				string.value = text.getText();
			}
		});
		this.add(text);
	}
	
}