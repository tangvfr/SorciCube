package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSwapCardGuiConfig extends BasicGuiConfig {

	public ItemNameGuiSwapCardGuiConfig itemName;

	public GuiSwapCardGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}