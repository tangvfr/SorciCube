package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class StatsGuiListFightGuiConfig extends AbstractConfig {

	public StringConfig waiting;
	public StringConfig starting;
	public StringConfig start;
	public StringConfig end;

	public StatsGuiListFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}