package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSwapCardGuiConfig extends CardsBasicGuiConfig {

	public StringConfig back;

	public GuiSwapCardGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}