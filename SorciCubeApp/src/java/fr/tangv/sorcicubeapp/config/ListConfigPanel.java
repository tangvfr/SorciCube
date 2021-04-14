package fr.tangv.sorcicubeapp.config;

import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.ListConfig;

public class ListConfigPanel extends ConfigPanel {

	private static final long serialVersionUID = 2541302758482910090L;

	public ListConfigPanel(MainConfigPanel main, ConfigPanel parent, ListConfig<? extends ElementConfig> list, String name) {
		super(main, parent, name);
		
	}

}
