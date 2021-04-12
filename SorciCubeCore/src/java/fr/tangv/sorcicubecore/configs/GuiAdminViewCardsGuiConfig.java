package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiAdminViewCardsGuiConfig extends AbstractConfig {

	public StringConfig name;
	public ItemNameGuiAdminViewCardsGuiConfig itemName;

	public GuiAdminViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}