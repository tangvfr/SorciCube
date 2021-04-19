package fr.tangv.sorcicubeapp.config;

import java.lang.reflect.Field;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;

public class AbstractConfigPanel extends ConfigPanel {

	private static final long serialVersionUID = 2541302758482910091L;
	private final AbstractConfig config;
	
	public AbstractConfigPanel(MainConfigPanel main, ConfigPanel parent, String name, AbstractConfig config) {
		super(main, parent, name);
		this.config = config;
		for (Field field : config.getClass().getFields())
			try {
				this.addComponent((ElementConfig) field.get(config), field.getName(), null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		this.repaint();
	}
	
	@Override
	public void back() {
		main.setView(new AbstractConfigPanel(main, parent, name, config));
	}
	
}