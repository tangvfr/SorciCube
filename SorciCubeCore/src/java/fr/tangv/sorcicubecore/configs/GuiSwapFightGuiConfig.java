package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSwapFightGuiConfig extends AbstractConfig {

	public StringConfig NAME;

	public GuiSwapFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}