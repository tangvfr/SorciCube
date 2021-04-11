package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiFightGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig UNCLASSIED;
	public StringConfig DUEL;
	public StringConfig CLOSE;

	public GuiFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}