package fr.tangv.sorcicubeapp.config;

import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.ElementConfig;
import fr.tangv.sorcicubecore.config.ListConfig;

public class ListConfigPanel extends ConfigPanel {

	private static final long serialVersionUID = 2541302758482910090L;
	private final ListConfig<? extends ElementConfig> list;
	
	public ListConfigPanel(MainConfigPanel main, ConfigPanel parent, String name, ListConfig<? extends ElementConfig> list) {
		super(main, parent, name);
		this.list = list;
		try {
			list.addNew(null);
		} catch (ConfigParseException e) {
			e.printStackTrace();
		}
		update();
	}
	
	public void update() {
		this.removeAll();
		for (int i = 0; i < list.size(); i++) {
			ElementConfig element = list.get(i);
			this.add(makeComponent(element, "["+i+"]"+element.nameString()));
		}
		this.repaint();
	}

}
