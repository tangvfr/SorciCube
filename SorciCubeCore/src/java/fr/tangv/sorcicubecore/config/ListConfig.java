package fr.tangv.sorcicubecore.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bson.Document;

public class ListConfig<E extends ElementConfig> extends Vector<E> implements ElementConfig {
	
	private static final long serialVersionUID = -9217705213133376301L;

	@SuppressWarnings("unchecked")
	public ListConfig(Document doc) throws ConfigParseException {
		super();
		if (doc != null) {
			List<Document> docs = doc.getList("list", Document.class);
			if (docs.size() > 0) {
				Class<?> type = docs.get(0).getClass();
				try {
					Constructor<?> construtor = type.getConstructor(Document.class);
					for (Document d : docs)
						this.add((E) construtor.newInstance(d));
				} catch (NoSuchMethodException e) {
					throw new ConfigParseException(type.getName()+" don't has constructor "+type.getSimpleName()+"(Document doc) !", e);
				} catch (SecurityException e) {
					throw new ConfigParseException(type.getName()+" don't has permission for constructor "+type.getSimpleName()+"(Document doc) !", e);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {
					throw new ConfigParseException(e.getClass().getSimpleName()+": "+e.getMessage(), e);
				}
			}
		}
	}
	
	@Override
	public Document toDocument() throws ConfigParseException {
		ArrayList<Document> docs = new ArrayList<Document>();
		for (E ob : this)
			docs.add(ob.toDocument());
		return new Document("list", docs);
	}

}
