package fr.tangv.sorcicubeapp.config;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ItemConfigPanel {

	public final JComponent comp;
	public final String label;
	
	public ItemConfigPanel(JComponent comp, String label) {
		this.comp = comp;
		this.label = label;
	}
	
	public JPanel createPanel(int weightLabel) {
		JPanel pan = new JPanel(new BorderLayout());
		
		//this.add(comp, BorderLayout.CENTER);
		return pan;
	}
	
}
