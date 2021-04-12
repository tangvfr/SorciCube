package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.ListConfig;

public class ArenasConfig extends ListConfig<ArenaConfig> {
	
	private static final long serialVersionUID = 6224063816995645077L;
	
	public ArenasConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
