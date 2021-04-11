package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiDecksGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig DECK;
	public StringConfig BACK;

	public GuiDecksGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}