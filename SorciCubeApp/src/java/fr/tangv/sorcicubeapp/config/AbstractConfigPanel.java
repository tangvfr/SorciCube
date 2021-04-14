package fr.tangv.sorcicubeapp.config;

import java.lang.reflect.Field;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ElementConfig;

public class AbstractConfigPanel extends ConfigPanel {

	private static final long serialVersionUID = 2541302758482910091L;

	public AbstractConfigPanel(MainConfigPanel main, ConfigPanel parent, String name, AbstractConfig config) {
		super(main, parent, name);
		for (Field field : config.getClass().getFields())
			try {
				this.add(makeComponent((ElementConfig) field.get(config), field.getName()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
	}
	
}