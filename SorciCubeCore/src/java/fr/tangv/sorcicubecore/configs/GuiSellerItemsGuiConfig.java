package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSellerItemsGuiConfig extends AbstractConfig {

	public StringConfig LORE_RIGHT;
	public StringConfig LORE_WRONG;

	public GuiSellerItemsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}