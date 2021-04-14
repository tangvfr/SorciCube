package fr.tangv.sorcicubeapp.config;

import fr.tangv.sorcicubecore.config.AbstractConfig;

public class AbstractConfigPanel extends ConfigPanel {

	private static final long serialVersionUID = 2541302758482910091L;

	public AbstractConfigPanel(MainConfigPanel main, ConfigPanel parent, AbstractConfig config, String name) {
		super(main, parent, name);
		
	}

	/*for (Field field : getClass().getFields()) {
		Class<?> type = field.getType();
		if (!ElementConfig.class.isAssignableFrom(type))
			throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
		try {
			try {
				Document d = (doc == null) ? null : doc.get(field.getName(), Document.class);
				field.set(this, type.getConstructor(Document.class).newInstance(d));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new ConfigParseException(e);
			}
		} catch (NoSuchMethodException e) {
			throw new ConfigParseException(type.getName()+" don't has constructor "+type.getSimpleName()+"(Document doc) !", e);
		} catch (SecurityException e) {
			throw new ConfigParseException(type.getName()+" don't has permission for constructor "+type.getSimpleName()+"(Document doc) !", e);
		}
	}*/
}