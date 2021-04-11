package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class BossBarGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig COLOR;
	public StringConfig NAME_ARENA;
	public StringConfig COLOR_ARENA;
	public StringConfig NAME_END;
	public StringConfig COLOR_END;

	public BossBarGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}