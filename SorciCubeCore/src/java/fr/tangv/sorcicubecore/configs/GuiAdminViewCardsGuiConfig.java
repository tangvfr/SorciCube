package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiAdminViewCardsGuiConfig extends BasicGuiConfig {

	public ItemNameGuiAdminViewCardsGuiConfig itemName;

	public GuiAdminViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}