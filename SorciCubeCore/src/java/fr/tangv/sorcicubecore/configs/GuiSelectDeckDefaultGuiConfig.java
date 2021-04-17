package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSelectDeckDefaultGuiConfig extends BasicGuiConfig {

	public StringConfig loreSelect;

	public GuiSelectDeckDefaultGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}