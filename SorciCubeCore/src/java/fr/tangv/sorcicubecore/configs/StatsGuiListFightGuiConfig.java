package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class StatsGuiListFightGuiConfig extends AbstractConfig {

	public StringConfig WAITING;
	public StringConfig STARTING;
	public StringConfig START;
	public StringConfig END;

	public StatsGuiListFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}