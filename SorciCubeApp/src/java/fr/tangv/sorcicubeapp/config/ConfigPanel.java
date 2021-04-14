package fr.tangv.sorcicubeapp.config;

import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;

import fr.tangv.sorcicubeapp.utils.ClickListener;
import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public abstract class ConfigPanel extends JTable {
	
	private static final long serialVersionUID = 3820881949873208818L;
	private static final String SEPARATOR = " > ";
	private final  MainConfigPanel main;
	protected final ConfigPanel parent;
	private final String name;
	
	protected ConfigPanel(MainConfigPanel main, ConfigPanel parent, String name) {
		this.main = main;
		this.parent = parent;
		this.name = name;
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
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
	
	public JComponent makeComponent(ElementConfig element, String name) {
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
		return comp;
	}
	
}
