package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSellerItemsGuiConfig extends AbstractConfig {

	public StringConfig loreRight;
	public StringConfig loreWrong;

	public GuiSellerItemsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}