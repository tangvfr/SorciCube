package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiDecksGuiConfig extends BasicGuiConfig {

	public StringConfig deck;
	public StringConfig back;

	public GuiDecksGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}