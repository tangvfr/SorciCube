package fr.tangv.sorcicubecore.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bson.Document;

public abstract class AbstractConfig implements ElementConfig {
	
	public AbstractConfig(Document doc) throws ConfigParseException {
		for (Field field : getClass().getFields()) {
			Class<?> type = field.getType();
			if (!ElementConfig.class.isAssignableFrom(type))
				throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
			try {
				try {
					Document d = (doc == null) ? null : doc.get(field.getName().toLowerCase(), Document.class);
					field.set(this, type.getConstructor(Document.class).newInstance(d));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new ConfigParseException(e);
				}
			} catch (NoSuchMethodException e) {
				throw new ConfigParseException(type.getName()+" don't has constructor "+type.getSimpleName()+"(Document doc) !", e);
			} catch (SecurityException e) {
				throw new ConfigParseException(type.getName()+" don't has permission for constructor "+type.getSimpleName()+"(Document doc) !", e);
			}
		}
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		Document doc = new Document();
		for (Field field : getClass().getFields()) {
			Class<?> type = field.getType();
			if (!ElementConfig.class.isAssignableFrom(type))
				throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
			try {
				Object ob = field.get(this);
				if (ob != null)
					doc.append(field.getName().toLowerCase(), ((ElementConfig) ob).toDocument());
			} catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
				throw new ConfigParseException(e.getClass().getSimpleName()+": "+e.getMessage(), e);
			}	
		}
		return doc;
	}
	
}
