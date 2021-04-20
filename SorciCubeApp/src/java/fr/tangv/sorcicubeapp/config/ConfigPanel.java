package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.config.LocationConfig;
import fr.tangv.sorcicubecore.config.StringConfig;
import fr.tangv.sorcicubecore.config.VectorConfig;

public abstract class ConfigPanel extends JPanel {
	
	private static final long serialVersionUID = 3820881949873208818L;
	private static final String SEPARATOR = " \u21AA";
	protected int[] scroll;
	protected final String name;
	protected final  MainConfigPanel main;
	protected final ConfigPanel parent;
	
	protected ConfigPanel(MainConfigPanel main, ConfigPanel parent, String name) {
		this.main = main;
		this.parent = parent;
		this.name = name;
		this.scroll = new int[2];
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}
	
	public String getPath() {
		String path = "";
		ConfigPanel pan = this;
		while (true)
			if (pan.parent != null) {
				path = SEPARATOR+pan.name+path;
				pan = pan.parent;
			} else {
				path = pan.name+path;
				break;
			}
		return path;
	}
	
	@SuppressWarnings("unchecked")
	public boolean enter(ElementConfig element, String name) {
		if (element instanceof AbstractConfig) {
			main.setView(new AbstractConfigPanel(main, this, name, (AbstractConfig) element));
			return true;
		} else if (element instanceof ListConfig<?>) {
			main.setView(new ListConfigPanel(main, this, name, (ListConfig<? extends ElementConfig>) element));
			return true;
		} else {
			return false;
		}
	}
	
	public void addComponent(ElementConfig element, String name, Runnable[] run) {
		JComponent comp;
		if (element == null) {
			comp = new ActionButtonConfigComponent(name, run);
		} else if (element instanceof BooleanConfig) {
			comp = new BooleanConfigComponent((BooleanConfig) element, name, run);
			
		} else if (element instanceof IntegerConfig) {
			comp = new IntegerConfigComponent((IntegerConfig) element, name, run);
			
		} else if (element instanceof StringConfig) {
			comp = new StringConfigComponent((StringConfig) element, name, run);
			
		} else if (element instanceof VectorConfig) {
			comp = new VectorConfigComponent((VectorConfig) element, name, run);
			
		} else if (element instanceof LocationConfig) {
			comp = new LocationConfigComponent((LocationConfig) element, name, run);
			
		} else if (element instanceof AbstractConfig || element instanceof ListConfig<?>) {
			comp = new ButtonConfigComponent(this, element, name, run);
			
		} else {
			comp = new JLabel("Unknown: "+name);
		}
		//----------------
		comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getMinimumSize().height));
		//----------------
		this.add(comp);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}
	
	public abstract void back();
	
}
