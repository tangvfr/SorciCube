package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiFightGuiConfig extends AbstractConfig {

	public StringConfig name;
	public StringConfig unclassied;
	public StringConfig duel;
	public StringConfig close;

	public GuiFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}