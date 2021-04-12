package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSwapCardGuiConfig extends AbstractConfig {

	public StringConfig name;
	public ItemNameGuiSwapCardGuiConfig itemName;

	public GuiSwapCardGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}