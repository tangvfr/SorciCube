package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiEditOrViewGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig DECK;
	public StringConfig CARD;
	public StringConfig CLOSE;

	public GuiEditOrViewGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}