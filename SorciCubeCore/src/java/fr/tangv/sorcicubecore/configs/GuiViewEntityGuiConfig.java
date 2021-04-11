package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiViewEntityGuiConfig extends AbstractConfig {

	public StringConfig NAME;

	public GuiViewEntityGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}