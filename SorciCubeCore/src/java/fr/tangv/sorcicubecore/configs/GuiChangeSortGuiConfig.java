package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiChangeSortGuiConfig extends BasicGuiConfig {

	public StringConfig back;

	public GuiChangeSortGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}