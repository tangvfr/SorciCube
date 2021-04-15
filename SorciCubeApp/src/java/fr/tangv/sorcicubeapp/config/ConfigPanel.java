package fr.tangv.sorcicubeapp.config;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public abstract class ConfigPanel extends JPanel {
	
	private static final long serialVersionUID = 3820881949873208818L;
	private static final String SEPARATOR = " > ";
	private final String name;
	protected final  MainConfigPanel main;
	protected final ConfigPanel parent;
	
	protected ConfigPanel(MainConfigPanel main, ConfigPanel parent, String name) {
		this.main = main;
		this.parent = parent;
		this.name = name;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
	
	public void addComponent(ElementConfig element, String name) {
		//create component
		JComponent comp;
		if (element instanceof BooleanConfig) {
			comp = new BooleanConfigComponent((BooleanConfig) element, name);
			
		} else if (element instanceof IntegerConfig) {
			comp = new IntegerConfigComponent((IntegerConfig) element, name);
			
		} else if (element instanceof StringConfig) {
			comp = new StringConfigComponent((StringConfig) element, name);
			
		} else if (element instanceof AbstractConfig || element instanceof ListConfig<?>) {
			JButton btn = new JButton(name);
			btn.addMouseListener(new ClickListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					enter(element, name);
				}
			});
			comp = btn;
			
		} else {
			comp = new JLabel("Unknown: "+name);
		}
		//size and display component
		//comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getMinimumSize().height));
		//add in panel component
		this.add(comp);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
	}
	
}
