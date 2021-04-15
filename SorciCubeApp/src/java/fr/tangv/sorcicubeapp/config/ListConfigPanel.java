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
		addComponent(null, "Add New", () -> {
			try {
				list.addNew(null);
				main.setView(new ListConfigPanel(main, parent, name, list));
			} catch (ConfigParseException e) {
				e.printStackTrace();
			}
		});
		for (int i = 0; i < list.size(); i++) {
			final int n = i;
			final ElementConfig element = list.get(n);
			this.addComponent(element, "["+n+"]"+element.nameString(), () -> {
				list.remove(n);
				main.setView(new ListConfigPanel(main, parent, name, list));
			});
		}
		this.repaint();
	}

	@Override
	public void back() {
		main.setView(new ListConfigPanel(main, parent, name, list));
	}

}
