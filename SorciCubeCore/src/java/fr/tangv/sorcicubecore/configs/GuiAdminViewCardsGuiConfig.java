package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiAdminViewCardsGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public ItemNameGuiAdminViewCardsGuiConfig ITEM_NAME;

	public GuiAdminViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}