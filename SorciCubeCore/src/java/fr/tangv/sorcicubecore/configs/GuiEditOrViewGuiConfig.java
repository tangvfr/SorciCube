package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiEditOrViewGuiConfig extends AbstractConfig {

	public StringConfig name;
	public StringConfig deck;
	public StringConfig card;
	public StringConfig close;

	public GuiEditOrViewGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}