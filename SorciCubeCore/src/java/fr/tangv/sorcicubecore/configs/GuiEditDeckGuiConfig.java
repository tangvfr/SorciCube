package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiEditDeckGuiConfig extends BasicGuiConfig {

	public StringConfig deck;
	public StringConfig back;
	public StringConfig loreFaction;

	public GuiEditDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}