package fr.tangv.sorcicubecore.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bson.Document;

public abstract class AbstractConfig implements ElementConfig {
	
	public AbstractConfig(Document doc) throws ConfigParseException {
		Class<?> clazz = getClass();
		for (Field field : clazz.getFields()) {
			Class<?> type = field.getType();
			if (!ElementConfig.class.isAssignableFrom(type))
				throw new ConfigParseException("ErrorField "+type.getName()+" don't has interface "+ElementConfig.class.getSimpleName()+" !");
			if (clazz.isAssignableFrom(type))
				throw new ConfigParseException("ErrorSameClass "+type.getName()+" some class can't have same class in his class !");
			try {
				try {
					Document d = (doc == null) ? null : doc.get(field.getName(), Document.class);
					field.set(this, ListConfig.class.isAssignableFrom(type) ?
						ListConfig.class.getConstructor(Document.class, Class.class).newInstance(d, clazz.getDeclaredField('_'+field.getName()).getType())
					:
						type.getConstructor(Document.class).newInstance(d)
					);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new ConfigParseException(e);
				}
			} catch (NoSuchFieldException e) {
				throw new ConfigParseException(type.getName()+" don't has extra field for "+type.getSimpleName()+" !", e);
			} catch (NoSuchMethodException e) {
				throw new ConfigParseException(type.getName()+" don't has valid constructor for "+type.getSimpleName()+" !", e);
			} catch (SecurityException e) {
				throw new ConfigParseException(type.getName()+" don't has permission for constructor of "+type.getSimpleName()+" !", e);
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
					doc.append(field.getName(), ((ElementConfig) ob).toDocument());
			} catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
				throw new ConfigParseException(e.getClass().getSimpleName()+": "+e.getMessage(), e);
			}	
		}
		return doc;
	}
	
}
