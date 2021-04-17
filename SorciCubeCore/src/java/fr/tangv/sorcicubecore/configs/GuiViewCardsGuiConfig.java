package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiViewCardsGuiConfig extends BasicGuiConfig {

	public ItemNameGuiViewCardsGuiConfig itemName;

	public GuiViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}