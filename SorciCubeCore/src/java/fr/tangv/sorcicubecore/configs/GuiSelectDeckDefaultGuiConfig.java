package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSelectDeckDefaultGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig LORE_SELECT;

	public GuiSelectDeckDefaultGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}